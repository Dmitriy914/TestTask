package example.repository;

import example.entity.Account;
import example.entity.Bank;
import example.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TransactionRepositoryIntegrationTest {
    @Autowired
    private TransactionRepository repository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankRepository bankRepository;

    private Bank createBank(){
        Bank bank = new Bank();
        bank.setName(RandomStringUtils.randomAlphabetic(10));
        bank.setAddress(RandomStringUtils.randomAlphabetic(10));
        bank.setPhone(RandomStringUtils.randomAlphabetic(10));
        return bankRepository.save(bank);
    }

    private User createUser(){
        User user = new User();
        user.setAddress(RandomStringUtils.randomAlphabetic(10));
        user.setName(RandomStringUtils.randomAlphabetic(10));
        user.setSurname(RandomStringUtils.randomAlphabetic(10));
        user.setPatronymic(RandomStringUtils.randomAlphabetic(10));
        user.setPhone(RandomStringUtils.randomAlphabetic(10));
        return userRepository.save(user);
    }

    private Account createAccount(User user, Bank bank){
        Account account = new Account();
        account.setUser(user);
        account.setBank(bank);
        account.setAccountNumber(RandomStringUtils.randomNumeric(10));
        account.setBalance(BigDecimal.ZERO);
        return accountRepository.save(account);
    }

    @Test
    public void test(){

    }
}
