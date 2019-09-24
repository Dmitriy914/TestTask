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
        return repository.save(account);
    }

    @Test
    public void findByUserId(){
        User user = createUserRandom();
        Account account = createAccountRandom(user, createBankRandom());

        Iterable<Account> findAccounts = repository.findByUserId(user.getId());

        assertThat(findAccounts, containsInAnyOrder(account));
    }

    @Test
    public void findByAccountNumber(){
        Account account = createAccountRandom(createUserRandom(), createBankRandom());

        Account findAccount = repository.findByAccountNumber(account.getAccountNumber()).orElse(null);

        assertNotNull(findAccount);
        assertEquals(account, findAccount);
    }

    @Test
    public void exists(){
        User user = createUserRandom();
        Bank bank = createBankRandom();
        Bank tmpBank = createBankRandom();
        Account account = createAccountRandom(user, bank);

        assertTrue(repository.existsByAccountNumber(account.getAccountNumber()));
        assertTrue(repository.existsByUserAndBank(user, bank));
        assertFalse(repository.existsByAccountNumber("12346"));
        assertFalse(repository.existsByUserAndBank(user, tmpBank));
    }
}
