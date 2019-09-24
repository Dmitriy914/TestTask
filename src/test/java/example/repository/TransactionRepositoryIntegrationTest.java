package example.repository;

import example.entity.Account;
import example.entity.Bank;
import example.entity.Transaction;
import example.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TransactionRepositoryIntegrationTest {
    @Autowired
    private TransactionRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankRepository bankRepository;

    private Bank createBankRandom(){
        Bank bank = new Bank();
        bank.setName(RandomStringUtils.randomAlphabetic(10));
        bank.setAddress(RandomStringUtils.randomAlphabetic(10));
        bank.setPhone(RandomStringUtils.randomAlphabetic(10));
        return bankRepository.save(bank);
    }

    private User createUserRandom(){
        User user = new User();
        user.setAddress(RandomStringUtils.randomAlphabetic(10));
        user.setName(RandomStringUtils.randomAlphabetic(10));
        user.setSurname(RandomStringUtils.randomAlphabetic(10));
        user.setPatronymic(RandomStringUtils.randomAlphabetic(10));
        user.setPhone(RandomStringUtils.randomAlphabetic(10));
        return userRepository.save(user);
    }

    private Account createAccountRandom(User user, Bank bank){
        Account account = new Account();
        account.setUser(user);
        account.setBank(bank);
        account.setAccountNumber(RandomStringUtils.randomNumeric(10));
        account.setBalance(BigDecimal.ZERO);
        return account;
    }

    private Transaction createTransaction(Account send, Account get, String amount, long millisec){
        Transaction transaction = new Transaction();
        transaction.setAccountSend(send);
        transaction.setAccountGet(get);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setDate(new Date(millisec));
        return repository.save(transaction);
    }

    @Test
    public void findByAccountSendAndAccountGetOrderByDateAsc(){
        Account send = createAccountRandom(createUserRandom(), createBankRandom());
        Account get = createAccountRandom(createUserRandom(), createBankRandom());
        Transaction transaction1 = createTransaction(send, get, "12", 1L);
        Transaction transaction2 = createTransaction(send, get, "21", 2L);

        Iterable<Transaction> findTransactionByDateAsc = repository.findByAccountSendAndAccountGetOrderByDateAsc(send, get);

        assertThat(findTransactionByDateAsc, contains(transaction1, transaction2));
    }

    @Test
    public void findByAccountSendAndAccountGetOrderByDateDesc(){
        Account send = createAccountRandom(createUserRandom(), createBankRandom());
        Account get = createAccountRandom(createUserRandom(), createBankRandom());
        Transaction transaction1 = createTransaction(send, get, "12", 1L);
        Transaction transaction2 = createTransaction(send, get, "21", 2L);

        Iterable<Transaction> findTransactionByDateDesc = repository.findByAccountSendAndAccountGetOrderByDateDesc(send, get);

        assertThat(findTransactionByDateDesc, contains(transaction2, transaction1));
    }

    @Test
    public void findByUser(){
        User user = createUserRandom();
        Transaction transaction1 = createTransaction(createAccountRandom(user, createBankRandom()), createAccountRandom(createUserRandom(), createBankRandom()), "12", 1L);
        Transaction transaction2 = createTransaction(createAccountRandom(createUserRandom(), createBankRandom()), createAccountRandom(user, createBankRandom()), "21", 2L);

        Iterable<Transaction> findByUser = repository.findByUser(user.getId());

        assertThat(findByUser, containsInAnyOrder(transaction1, transaction2));
    }

    @Test
    public void findByUserAndBank(){
        User user = createUserRandom();
        Bank bank = createBankRandom();
        Account account = createAccountRandom(user, bank);
        Transaction transaction1 = createTransaction(account, createAccountRandom(createUserRandom(), bank), "123", 1L);
        Transaction transaction2 = createTransaction(createAccountRandom(createUserRandom(), createBankRandom()), account, "132", 2L);
        createTransaction(createAccountRandom(user, createBankRandom()), createAccountRandom(createUserRandom(), bank), "213", 3L);
        createTransaction(createAccountRandom(createUserRandom(), createBankRandom()), createAccountRandom(createUserRandom(), createBankRandom()), "231", 4L);

        Iterable<Transaction> findByUser = repository.findByUserAndBank(user.getId(), bank.getId());

        assertThat(findByUser, containsInAnyOrder(transaction1, transaction2));
    }

}
