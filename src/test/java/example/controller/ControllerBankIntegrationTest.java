package example.controller;

import example.entity.Bank;
import example.model.BankModel;
import example.repository.BankRepository;
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

    @Test
    public void saveBankWithStatusOk(){
        // подготавливаю данные к сохранению
        BankModel model = new BankModel();
        model.setName("Name");
        model.setPhone("Phone");
        model.setAddress("Address");

        // сохраняю модель
        ResponseEntity<Bank> response = restTemplate.postForEntity("http://localhost:" + port + "/banks", model, Bank.class);

        // подготавливаю результаты запроса к проверкам
        Bank body = response.getBody();
        Bank bank = repository.findByName("Name").orElse(null);

        // выполняю проверки контроллера
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        assertEquals((Integer) 1, body.getId());
        assertEquals("Name", body.getName());
        assertEquals("Phone", body.getPhone());
        assertEquals("Address", body.getAddress());

        // выполняю проверки репозитория
        assertNotNull(bank);
        assertEquals(body, bank);
    }

    @Test
    public void errorSaveBankWithStatusBadRequest(){
        BankModel model = new BankModel();
        model.setName("");
        model.setPhone("Phone1");
        model.setAddress("Address1");

        ResponseEntity<String[]> response = restTemplate.postForEntity("http://localhost:" + port + "/banks", model, String[].class);
        String[] body = response.getBody();

        // проверки контроллера
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(body);
        assertEquals(1, body.length);
        assertEquals("Field (name) should not be empty", body[0]);

        // проверка репозитория
        assertFalse(repository.existsByPhone("Phone1"));
    }
}
