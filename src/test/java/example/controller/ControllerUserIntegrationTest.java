package example.controller;

import example.entity.Account;
import example.entity.Bank;
import example.entity.Transaction;
import example.entity.User;
import example.model.UserModel;
import example.repository.AccountRepository;
import example.repository.BankRepository;
import example.repository.TransactionRepository;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class ControllerUserIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository repository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private UserModel createUserModelRandom(){
        UserModel model = new UserModel();
        model.setName(RandomStringUtils.randomAlphabetic(10));
        model.setPhone(RandomStringUtils.randomAlphabetic(10));
        model.setAddress(RandomStringUtils.randomAlphabetic(10));
        model.setPatronymic(RandomStringUtils.randomAlphabetic(10));
        model.setSurname(RandomStringUtils.randomAlphabetic(10));
        return model;
    }

    private User createAndSaveUserRandom(){
        User user = new User();
        user.setName(RandomStringUtils.randomAlphabetic(10));
        user.setSurname(RandomStringUtils.randomAlphabetic(10));
        user.setAddress(RandomStringUtils.randomAlphabetic(10));
        user.setPhone(RandomStringUtils.randomAlphabetic(10));
        user.setPatronymic(RandomStringUtils.randomAlphabetic(10));
        return repository.save(user);
    }

    private Bank createAndSaveBankRandom(){
        Bank bank = new Bank();
        bank.setName(RandomStringUtils.randomAlphabetic(10));
        bank.setAddress(RandomStringUtils.randomAlphabetic(10));
        bank.setPhone(RandomStringUtils.randomAlphabetic(10));
        return bankRepository.save(bank);
    }

    private Account createAndSaveAccountRandom(User user, Bank bank){
        Account account = new Account();
        account.setUser(user);
        account.setBank(bank);
        account.setBalance(BigDecimal.ZERO.setScale(2));
        account.setAccountNumber(RandomStringUtils.randomAlphabetic(10));
        return accountRepository.save(account);
    }

    private Transaction createAndSaveTransaction(Account send, Account get, String amount, long millisec){
        Transaction transaction = new Transaction();
        transaction.setAccountSend(send);
        transaction.setAccountGet(get);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setDate(new Date(millisec));
        return transactionRepository.save(transaction);
    }

    @Test
    public void saveUserWithStatusOk(){
        UserModel model = createUserModelRandom();

        ResponseEntity<User> response = restTemplate.postForEntity("http://localhost:" + port + "/users", model, User.class);

        User responseBody = response.getBody();
        User savedUser = repository.findByPhone(model.getPhone()).orElse(null);

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
        assertEquals(404, response.getBody().get("status"));
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
