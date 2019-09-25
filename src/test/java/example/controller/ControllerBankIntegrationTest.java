package example.controller;

import example.entity.Bank;
import example.model.BankModel;
import example.repository.BankRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class ControllerBankIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BankRepository repository;

    @Before
    public void clean(){
        repository.deleteAll();
    }

    private BankModel createBankModelRandom(){
        BankModel model = new BankModel();
        model.setName(RandomStringUtils.randomAlphabetic(10));
        model.setAddress(RandomStringUtils.randomAlphabetic(10));
        model.setPhone(RandomStringUtils.randomAlphabetic(10));
        return model;
    }

    private Bank createAndSaveBankRandom(){
        Bank bank = new Bank();
        bank.setName(RandomStringUtils.randomAlphabetic(10));
        bank.setAddress(RandomStringUtils.randomAlphabetic(10));
        bank.setPhone(RandomStringUtils.randomAlphabetic(10));
        return repository.save(bank);
    }

    @Test
    public void saveBankWithStatusOk(){
        BankModel model = createBankModelRandom();

        ResponseEntity<Bank> response = restTemplate.postForEntity("http://localhost:" + port + "/banks", model, Bank.class);

        Bank responseBody = response.getBody();
        Bank savedBank = repository.findByName(model.getName()).orElse(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(savedBank, responseBody);
    }

    @Test
    public void errorSaveBankWithStatusBadRequest(){
        BankModel errorModel = createBankModelRandom();
        errorModel.setName("");

        ResponseEntity<String[]> response = restTemplate.postForEntity("http://localhost:" + port + "/banks", errorModel, String[].class);

        String[] responseBody = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertThat(responseBody, arrayContainingInAnyOrder("Field (name) should not be empty"));
    }

    @Test
    public void searchAll(){
        Bank bank1 = createAndSaveBankRandom();
        Bank bank2 = createAndSaveBankRandom();

        Bank[] banks = restTemplate.getForObject("http://localhost:" + port + "/banks", Bank[].class);

        assertEquals(2, banks.length);
        assertThat(banks, arrayContainingInAnyOrder(bank1, bank2));
    }
}
