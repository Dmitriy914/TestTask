package example.controller;

import example.entity.Account;
import example.entity.Bank;
import example.entity.User;
import example.model.AccountModel;
import example.repository.AccountRepository;
import example.repository.BankRepository;
import example.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
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

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class ControllerAccountIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankRepository bankRepository;

    private User createUserRandom(){
        User user = new User();
        user.setSurname(RandomStringUtils.randomAlphabetic(10));
        user.setPhone(RandomStringUtils.randomAlphabetic(10));
        user.setAddress(RandomStringUtils.randomAlphabetic(10));
        user.setName(RandomStringUtils.randomAlphabetic(10));
        user.setPatronymic(RandomStringUtils.randomAlphabetic(10));
        return userRepository.save(user);
    }

    private Bank createBankRandom(){
        Bank bank = new Bank();
        bank.setName(RandomStringUtils.randomAlphabetic(10));
        bank.setAddress(RandomStringUtils.randomAlphabetic(10));
        bank.setPhone(RandomStringUtils.randomAlphabetic(10));
        return bankRepository.save(bank);
    }

    private AccountModel createAccountModelRandom(String user, String bank){
        AccountModel model = new AccountModel();
        model.setUserIdOrPhone(user);
        model.setBankIdOrNameOrPhone(bank);
        return model;
    }

    @Test
    public void saveAccountWithStatusOk(){
        User user = createUserRandom();
        Bank bank = createBankRandom();
        AccountModel model = createAccountModelRandom(user.getPhone(), bank.getPhone());

        ResponseEntity<Account> response = restTemplate.postForEntity("http://localhost:" + port + "/accounts", model, Account.class);

        Account responseBody = response.getBody();
        Iterable<Account> saveAccount = repository.findByUserId(user.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertThat(saveAccount, contains(responseBody));
    }

    @Test
    public void errorSaveAccountWithStatusBadRequest(){

    }
}
