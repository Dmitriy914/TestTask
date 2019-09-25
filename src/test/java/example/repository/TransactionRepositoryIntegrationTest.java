package example.repository;

import example.entity.Account;
import example.entity.Bank;
import example.entity.Transaction;
import example.entity.User;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class TransactionRepositoryIntegrationTest extends SuperRepositoryTest{
    @Test
    public void findByAccountSendAndAccountGetOrderByDateAsc(){
        Account send = createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom());
        Account get = createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom());
        Transaction transaction1 = createAndSaveTransaction(send, get, "12", 1L);
        Transaction transaction2 = createAndSaveTransaction(send, get, "21", 2L);

        Iterable<Transaction> foundTransactionOrderByDateAsc = transactionRepository.findByAccountSendAndAccountGetOrderByDateAsc(send, get);

        assertThat(foundTransactionOrderByDateAsc, contains(transaction1, transaction2));
    }

    @Test
    public void findByAccountSendAndAccountGetOrderByDateDesc(){
        Account send = createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom());
        Account get = createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom());
        Transaction transaction1 = createAndSaveTransaction(send, get, "12", 1L);
        Transaction transaction2 = createAndSaveTransaction(send, get, "21", 2L);

        Iterable<Transaction> foundTransactionOrderByDateDesc = transactionRepository.findByAccountSendAndAccountGetOrderByDateDesc(send, get);

        assertThat(foundTransactionOrderByDateDesc, contains(transaction2, transaction1));
    }

    @Test
    public void findByUser(){
        User user = createAndSaveUserRandom();

        Transaction transaction1 = createAndSaveTransaction(
                createAndSaveAccountRandom(user, createAndSaveBankRandom()),
                createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom()),"12", 1L);

        Transaction transaction2 = createAndSaveTransaction(
                createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom()),
                createAndSaveAccountRandom(user, createAndSaveBankRandom()), "21", 2L);

        Iterable<Transaction> foundByUser = transactionRepository.findByUser(user.getId());

        assertThat(foundByUser, containsInAnyOrder(transaction1, transaction2));
    }

    @Test
    public void findByUserAndBank(){
        User user = createAndSaveUserRandom();
        Bank bank = createAndSaveBankRandom();
        Account account = createAndSaveAccountRandom(user, bank);

        Transaction transaction1 = createAndSaveTransaction(account,
                createAndSaveAccountRandom(createAndSaveUserRandom(), bank), "123", 1L);

        Transaction transaction2 = createAndSaveTransaction(
                createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom()),
                account, "132", 2L);

        createAndSaveTransaction(createAndSaveAccountRandom(user, createAndSaveBankRandom()),
                createAndSaveAccountRandom(createAndSaveUserRandom(), bank), "213", 3L);

        createAndSaveTransaction(createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom()),
                createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom()), "231", 4L);

        Iterable<Transaction> foundByUser = transactionRepository.findByUserAndBank(user.getId(), bank.getId());

        assertThat(foundByUser, containsInAnyOrder(transaction1, transaction2));
    }
}
