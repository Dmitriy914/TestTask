package example.repository;

import example.entity.Account;
import example.entity.Bank;
import example.entity.Transaction;
import example.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.Instant;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@RunWith(SpringRunner.class)
@DataJpaTest
public abstract class RepositoryTest {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BankRepository bankRepository;

    @Autowired
    TransactionRepository transactionRepository;

    Account createAccountRandom(User user, Bank bank){
        Account account = new Account();
        account.setUser(user);
        account.setBank(bank);
        account.setBalance(new BigDecimal("100.00"));
        account.setAccountNumber(RandomStringUtils.randomNumeric(10));
        return account;
    }

    User createAndSaveUserRandom(){
        User user = new User();
        user.setSurname(randomAlphabetic(10));
        user.setPhone(randomAlphabetic(10));
        user.setAddress(randomAlphabetic(10));
        user.setName(randomAlphabetic(10));
        user.setPatronymic(randomAlphabetic(10));
        return userRepository.save(user);
    }

    Bank createAndSaveBankRandom(){
        Bank bank = new Bank();
        bank.setName(randomAlphabetic(10));
        bank.setAddress(randomAlphabetic(10));
        bank.setPhone(randomAlphabetic(10));
        return bankRepository.save(bank);
    }

    Account createAndSaveAccountRandom(User user, Bank bank){
        Account account = new Account();
        account.setUser(user);
        account.setBank(bank);
        account.setBalance(new BigDecimal("100.00"));
        account.setAccountNumber(RandomStringUtils.randomNumeric(10));
        return accountRepository.save(account);
    }

    Transaction createAndSaveTransaction(Account send, Account get, String amount, long millisec){
        Transaction transaction = new Transaction();
        transaction.setAccountSend(send);
        transaction.setAccountGet(get);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setInstant(Instant.ofEpochMilli(millisec));
        return transactionRepository.save(transaction);
    }
}
