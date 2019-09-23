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

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class ControllerBankIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void saveBankWithStatusOk(){
        // подготавливаю данные к сохранению
        BankModel model = new BankModel();
        model.setName("Name");
        model.setPhone("Phone");
        model.setAddress("Address");

        // сохраняю модель
        ResponseEntity<Bank> response = restTemplate.postForEntity("http://localhost:" + port + "/banks", model, Bank.class);

        // подготавливаю результат запроса к проверкам
        Bank body = response.getBody();

        // выполняю проверки
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        assertEquals((Integer) 1, body.getId());
        assertEquals("Name", body.getName());
        assertEquals("Phone", body.getPhone());
        assertEquals("Address", body.getAddress());
    }

    @Test
    public void errorSaveBankWithStatusBadRequest(){
        BankModel model = new BankModel();
        model.setName("");
        model.setPhone("Phone1");
        model.setAddress("Address1");

        ResponseEntity<String[]> response = restTemplate.postForEntity("http://localhost:" + port + "/banks", model, String[].class);

        String[] body = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(body);
        assertEquals(1, body.length);
        assertEquals("Field (name) should not be empty", body[0]);
    }

    @Test
    public void searchAll(){
        Bank[] banks = restTemplate.getForObject("http://localhost:" + port + "/banks", Bank[].class);

        assertEquals(1, banks.length);
        assertEquals("Name", banks[0].getName());
        assertEquals("Phone", banks[0].getPhone());
        assertEquals("Address", banks[0].getAddress());
    }
}
