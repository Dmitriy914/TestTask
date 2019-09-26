package example.controller;

import example.entity.Account;
import example.entity.Transaction;
import example.entity.User;
import example.model.TransactionModel;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.junit.Assert.*;

public class ControllerTransactionIntegrationTest extends SuperControllerTest{

    @Test
    public void saveTransactionWithStatusOk(){
        User user = createAndSaveUserRandom();
        TransactionModel model = createTransactionModel(
                createAndSaveAccountRandom(user, createAndSaveBankRandom()),
                createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom()), "12");

        ResponseEntity<Transaction> response = restTemplate.postForEntity("http://localhost:" + port + "/transactions", model, Transaction.class);

        Transaction responseBody = response.getBody();
        ArrayList<Transaction> savedTransaction = Lists.newArrayList(transactionRepository.findByUser(user.getId()));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(1, savedTransaction.size());
        assertEquals(savedTransaction.get(0).getAccountGet(), responseBody.getAccountGet());
        assertEquals(savedTransaction.get(0).getAccountSend(), responseBody.getAccountSend());
        assertEquals(0, responseBody.getAmount().compareTo(savedTransaction.get(0).getAmount()));
    }

    @Test
    public void errorSaveTransactionWithStatusBadRequest(){
        TransactionModel model = new TransactionModel(
                "", "1", new BigDecimal("0"));

        ResponseEntity<String[]> response = restTemplate.postForEntity("http://localhost:" + port + "/transactions", model, String[].class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
        assertThat(response.getBody(), arrayContainingInAnyOrder("Field (accountSendIdOrAccountNumber) should not be empty", "Field (amount) should be positive"));
    }

    @Test
    public void errorSaveTransactionWithStatusNotFound(){
        TransactionModel model = new TransactionModel(
                "1", "2", new BigDecimal("12"));

        ResponseEntity<HashMap> response = restTemplate.postForEntity("http://localhost:" + port + "/transactions", model, HashMap.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Account send not found", response.getBody().get("message"));
    }

    @Test
    public void searchTransactionAsc(){
        Account accountGet = createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom());
        Account accountSend = createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom());
        Transaction transaction1 = createAndSaveTransaction(accountSend, accountGet, "12", 1L);
        Transaction transaction2 = createAndSaveTransaction(accountSend, accountGet, "21", 2L);

        ResponseEntity<Transaction[]> response = restTemplate.getForEntity("http://localhost:" + port
                + "/transactions?accountSendIdOrAccountNumber={send}&accountGetIdOrAccountNumber={get}&sortMode={mode}",
                Transaction[].class, accountSend.getAccountNumber(), accountGet.getAccountNumber(), "Asc");

        Transaction[] responseBody = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(2, responseBody.length);

        assertEquals(responseBody[0].getAccountGet(), transaction1.getAccountGet());
        assertEquals(responseBody[0].getAccountSend(), transaction1.getAccountSend());
        assertEquals(0, responseBody[0].getAmount().compareTo(transaction1.getAmount()));

        assertEquals(responseBody[1].getAccountGet(), transaction2.getAccountGet());
        assertEquals(responseBody[1].getAccountSend(), transaction2.getAccountSend());
        assertEquals(0, responseBody[1].getAmount().compareTo(transaction2.getAmount()));
    }

    @Test
    public void searchTransactionDesc(){
        Account accountGet = createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom());
        Account accountSend = createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom());
        Transaction transaction1 = createAndSaveTransaction(accountSend, accountGet, "12", 1L);
        Transaction transaction2 = createAndSaveTransaction(accountSend, accountGet, "21", 2L);

        ResponseEntity<Transaction[]> response = restTemplate.getForEntity("http://localhost:" + port
                        + "/transactions?accountSendIdOrAccountNumber={send}&accountGetIdOrAccountNumber={get}&sortMode={mode}",
                Transaction[].class, accountSend.getAccountNumber(), accountGet.getAccountNumber(), "Desc");

        Transaction[] responseBody = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(2, responseBody.length);

        assertEquals(responseBody[0].getAccountGet(), transaction2.getAccountGet());
        assertEquals(responseBody[0].getAccountSend(), transaction2.getAccountSend());
        assertEquals(0, responseBody[0].getAmount().compareTo(transaction2.getAmount()));

        assertEquals(responseBody[1].getAccountGet(), transaction1.getAccountGet());
        assertEquals(responseBody[1].getAccountSend(), transaction1.getAccountSend());
        assertEquals(0, responseBody[1].getAmount().compareTo(transaction1.getAmount()));
    }

    @Test
    public void errorSearchTransactionWithStatusBadRequest(){
        ResponseEntity<HashMap> response = restTemplate.getForEntity("http://localhost:" + port
                + "/transactions?accountSendIdOrAccountNumber={send}&accountGetIdOrAccountNumber={get}&sortMode={mode}", HashMap.class, "1", "2", "Ask");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Parameter (sortMode) should be (Asc) or (Desc)", response.getBody().get("message"));
    }

    @Test
    public void errorSearchTransactionWithStatusNotFound(){
        ResponseEntity<HashMap> response = restTemplate.getForEntity("http://localhost:" + port
                + "/transactions?accountSendIdOrAccountNumber={send}&accountGetIdOrAccountNumber={get}&sortMode={mode}", HashMap.class, "100", "2", "Asc");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Account send not found", response.getBody().get("message"));
    }

    @Test
    public void test(){
        User user = createAndSaveUserRandom();
        Transaction transaction = createAndSaveTransaction(
                createAndSaveAccountRandom(user, createAndSaveBankRandom()),
                createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom()), "12", 1L);

        Iterable<Transaction> foundByUser = transactionRepository.findByUser(user.getId());

        System.out.println(transaction);
        System.out.println(foundByUser.iterator().next());

        System.out.println("Дата в объекте при сохранении:" + transaction.getDate());
        System.out.println("Дата в объекте при поиске    :" + foundByUser.iterator().next().getDate());
    }
}
