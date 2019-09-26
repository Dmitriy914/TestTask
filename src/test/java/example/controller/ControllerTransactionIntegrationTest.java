package example.controller;

import example.entity.Transaction;
import example.entity.User;
import example.model.TransactionModel;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static org.hamcrest.Matchers.contains;
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
        
        Iterable<Transaction> savedTransaction = transactionRepository.findByUser(user.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertThat(savedTransaction, contains(responseBody));
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
