package example.controller;

import example.entity.Account;
import example.entity.Bank;
import example.entity.Transaction;
import example.entity.User;
import example.model.UserModel;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.junit.Assert.*;

public class ControllerUserIntegrationTest extends SuperControllerTest{

    @Test
    public void saveUserWithStatusOk(){
        UserModel model = createUserModelRandom();

        ResponseEntity<User> response = restTemplate.postForEntity("http://localhost:" + port + "/users", model, User.class);

        User responseBody = response.getBody();
        User savedUser = userRepository.findByPhone(model.getPhone()).orElse(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(savedUser, responseBody);
    }

    @Test
    public void errorSaveUserWithStatusBadRequest(){
        UserModel errorModel = createUserModelRandom();
        errorModel.setName("");
        errorModel.setSurname("");

        ResponseEntity<String[]> response = restTemplate.postForEntity("http://localhost:" + port + "/users", errorModel, String[].class);

        String[] responseBody = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertThat(responseBody, arrayContainingInAnyOrder("Field (name) should not be empty", "Field (surname) should not be empty"));
    }

    @Test
    public void searchWithStatusOk(){
        User user = createAndSaveUserRandom();

        ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:" + port + "/users/{id}", User.class, user.getId());

        User foundUser = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, foundUser);
    }

    @Test
    public void searchWithStatusNotFound(){
        ResponseEntity<HashMap> response = restTemplate.getForEntity("http://localhost:" + port + "/users/{id}", HashMap.class, 100);

        System.out.println(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody().get("message"));
    }

    @Test
    public void getAccounts(){
        User user = createAndSaveUserRandom();
        Account account1 = createAndSaveAccountRandom(user, createAndSaveBankRandom());
        Account account2 = createAndSaveAccountRandom(user, createAndSaveBankRandom());

        ResponseEntity<Account[]> response = restTemplate.getForEntity("http://localhost:" + port + "/users/{id}/accounts", Account[].class, user.getId());

        Account[] foundAccounts = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(foundAccounts, arrayContainingInAnyOrder(account1, account2));
    }

    @Test
    public void getTransactionByUser(){
        User user = createAndSaveUserRandom();

        Transaction transaction1 = createAndSaveTransaction(
                createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom()),
                createAndSaveAccountRandom(user, createAndSaveBankRandom()), "12.00", 1L);

        Transaction transaction2 = createAndSaveTransaction(
                createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom()),
                createAndSaveAccountRandom(user, createAndSaveBankRandom()), "21.00", 1L);

        ResponseEntity<Transaction[]> response = restTemplate.getForEntity("http://localhost:" + port + "/users/{id}/transactions", Transaction[].class, user.getId());

        Transaction[] foundTransactions = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(foundTransactions, arrayContainingInAnyOrder(transaction1, transaction2));
    }

    @Test
    public void getTransactionByUserAndBank(){
        User user = createAndSaveUserRandom();
        Bank bank = createAndSaveBankRandom();
        Account account = createAndSaveAccountRandom(user, bank);

        Transaction transaction1 = createAndSaveTransaction(
                createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom()),
                account, "123.00", 1L);

        createAndSaveTransaction(
                createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom()),
                createAndSaveAccountRandom(user, createAndSaveBankRandom()), "132.00", 1L);

        createAndSaveTransaction(
                createAndSaveAccountRandom(user, createAndSaveBankRandom()),
                createAndSaveAccountRandom(createAndSaveUserRandom(), bank), "213.00", 1L);

        Transaction transaction4 = createAndSaveTransaction(account,
                createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom()), "231.00", 1L);

        ResponseEntity<Transaction[]> response = restTemplate.getForEntity("http://localhost:" + port + "/users/{id}/transactions?bankIdOrNameOrPhone={bank}", Transaction[].class, user.getId(), bank.getId());

        Transaction[] foundTransactions = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(foundTransactions, arrayContainingInAnyOrder(transaction1, transaction4));
    }
}
