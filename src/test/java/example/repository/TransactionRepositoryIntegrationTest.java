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

    @Autowired
    private AccountRepository accountRepository;

    private Bank createAndSaveBankRandom(){
        Bank bank = new Bank();
        bank.setName(RandomStringUtils.randomAlphabetic(10));
        bank.setAddress(RandomStringUtils.randomAlphabetic(10));
        bank.setPhone(RandomStringUtils.randomAlphabetic(10));
        return bankRepository.save(bank);
    }

    private User createAndSaveUserRandom(){
        User user = new User();
        user.setAddress(RandomStringUtils.randomAlphabetic(10));
        user.setName(RandomStringUtils.randomAlphabetic(10));
        user.setSurname(RandomStringUtils.randomAlphabetic(10));
        user.setPatronymic(RandomStringUtils.randomAlphabetic(10));
        user.setPhone(RandomStringUtils.randomAlphabetic(10));
        return userRepository.save(user);
    }

    private Account createAndSaveAccountRandom(User user, Bank bank){
        Account account = new Account();
        account.setUser(user);
        account.setBank(bank);
        account.setAccountNumber(RandomStringUtils.randomNumeric(10));
        account.setBalance(BigDecimal.ZERO);
        return accountRepository.save(account);
    }

    private Transaction createAndSaveTransaction(Account send, Account get, String amount, long millisec){
        Transaction transaction = new Transaction();
        transaction.setAccountSend(send);
        transaction.setAccountGet(get);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setDate(new Date(millisec));
        return repository.save(transaction);
    }

    @Test
    public void findByAccountSendAndAccountGetOrderByDateAsc(){
        Account send = createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom());
        Account get = createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom());
        Transaction transaction1 = createAndSaveTransaction(send, get, "12", 1L);
        Transaction transaction2 = createAndSaveTransaction(send, get, "21", 2L);

        Iterable<Transaction> foundTransactionOrderByDateAsc = repository.findByAccountSendAndAccountGetOrderByDateAsc(send, get);

        assertThat(foundTransactionOrderByDateAsc, contains(transaction1, transaction2));
    }

    @Test
    public void findByAccountSendAndAccountGetOrderByDateDesc(){
        Account send = createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom());
        Account get = createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom());
        Transaction transaction1 = createAndSaveTransaction(send, get, "12", 1L);
        Transaction transaction2 = createAndSaveTransaction(send, get, "21", 2L);

        Iterable<Transaction> foundTransactionOrderByDateDesc = repository.findByAccountSendAndAccountGetOrderByDateDesc(send, get);

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

        Iterable<Transaction> foundByUser = repository.findByUser(user.getId());

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

        Iterable<Transaction> foundByUser = repository.findByUserAndBank(user.getId(), bank.getId());

        assertThat(foundByUser, containsInAnyOrder(transaction1, transaction2));
    }
}
