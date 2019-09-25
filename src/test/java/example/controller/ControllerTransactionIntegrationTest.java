package example.controller;

import example.entity.Transaction;
import example.entity.User;
import example.model.TransactionModel;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

public class ControllerTransactionIntegrationTest extends SuperTest{

    @Test
    public void saveTransactionWithStatusOk(){
        User user = createAndSaveUserRandom();
        TransactionModel model = createTransactionModelRandom(
                createAndSaveAccountRandom(user, createAndSaveBankRandom()),
                createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom()), "12.00");

        ResponseEntity<Transaction> response = restTemplate.postForEntity("http://localhost:" + port + "/transactions", model, Transaction.class);

        //ResponseEntity<HashMap> response = restTemplate.postForEntity("http://localhost:" + port + "/transactions", model, HashMap.class);
        //System.out.println(response.getBody());

        Transaction responseBody = response.getBody();
        Iterable<Transaction> savedTransaction = transactionRepository.findByUser(user.getId());

        System.out.println(savedTransaction.iterator().next().getDate());
        System.out.println(responseBody.getDate());
        System.out.println(savedTransaction.iterator().next().equals(responseBody));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertThat(savedTransaction, contains(responseBody));
    }
}