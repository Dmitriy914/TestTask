package example.controller;

import example.entity.Account;
import example.entity.Bank;
import example.entity.User;
import example.model.AccountModel;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ControllerAccountIntegrationTest extends ControllerTest{
    @Test
    public void saveAccountWithStatusOk(){
        User user = createAndSaveUserRandom();
        Bank bank = createAndSaveBankRandom();
        AccountModel model = new AccountModel(user.getPhone(), bank.getPhone());

        ResponseEntity<Account> response = restTemplate.postForEntity("http://localhost:" + port + "/accounts", model, Account.class);

        Account responseBody = response.getBody();
        Iterable<Account> savedAccount = accountRepository.findByUserId(user.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertThat(savedAccount, contains(responseBody));
    }

    @Test
    public void errorSaveAccountWithStatusBadRequest(){
        AccountModel errorModel = new AccountModel("userId", "");

        ResponseEntity<String[]> response = restTemplate.postForEntity("http://localhost:" + port + "/accounts", errorModel, String[].class);

        String[] responseBody = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertThat(responseBody, arrayContainingInAnyOrder("Field (bankIdOrNameOrPhone) should not be empty"));
    }

    @Test
    public void errorSaveAccountWithStatusNotFound(){
        User user = createAndSaveUserRandom();
        AccountModel model = new AccountModel(user.getPhone(), "10");

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/accounts", model, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Bank not found", response.getBody());
    }
}
