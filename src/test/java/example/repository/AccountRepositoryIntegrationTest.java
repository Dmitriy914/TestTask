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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryIntegrationTest {
    @Autowired
    private AccountRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankRepository bankRepository;

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
        return repository.save(account);
    }

    @Test
    public void findByUserId(){
        User user = createAndSaveUserRandom();
        Account account1 = createAndSaveAccountRandom(user, createAndSaveBankRandom());
        Account account2 = createAndSaveAccountRandom(user, createAndSaveBankRandom());

        Iterable<Account> foundAccounts = repository.findByUserId(user.getId());

        assertThat(foundAccounts, containsInAnyOrder(account1, account2));
    }

    @Test
    public void findByAccountNumber(){
        Account account = createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom());

        Account foundAccount = repository.findByAccountNumber(account.getAccountNumber()).orElse(null);

        assertEquals(account, foundAccount);
    }

    @Test
    public void exists(){
        User user = createAndSaveUserRandom();
        Bank userBank = createAndSaveBankRandom();
        Bank freeBank = createAndSaveBankRandom();
        Account account = createAndSaveAccountRandom(user, userBank);

        assertTrue(repository.existsByAccountNumber(account.getAccountNumber()));
        assertTrue(repository.existsByUserAndBank(user, userBank));
        assertFalse(repository.existsByAccountNumber("NotExistsAccountNumber"));
        assertFalse(repository.existsByUserAndBank(user, freeBank));
    }
}
