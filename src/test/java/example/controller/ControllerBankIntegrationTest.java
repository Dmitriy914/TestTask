package example.controller;

import example.entity.Bank;
import example.model.BankModel;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.junit.Assert.*;

public class ControllerBankIntegrationTest extends SuperControllerTest{

    @Test
    public void saveBankWithStatusOk(){
        BankModel model = createBankModelRandom();

        ResponseEntity<Bank> response = restTemplate.postForEntity("http://localhost:" + port + "/banks", model, Bank.class);

        Bank responseBody = response.getBody();
        Bank savedBank = bankRepository.findByName(model.getName()).orElse(null);

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
        bankRepository.deleteAll();
        Bank bank1 = createAndSaveBankRandom();
        Bank bank2 = createAndSaveBankRandom();

        Bank[] banks = restTemplate.getForObject("http://localhost:" + port + "/banks", Bank[].class);

        assertEquals(2, banks.length);
        assertThat(banks, arrayContainingInAnyOrder(bank1, bank2));
    }
}
