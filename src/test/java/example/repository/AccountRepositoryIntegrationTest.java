package example.repository;

import example.entity.Account;
import example.entity.Bank;
import example.entity.User;
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

    private Bank createBank(){
        Bank bank = new Bank();
        bank.setName("Name");
        bank.setAddress("Address");
        bank.setPhone("Phone");
        return bankRepository.save(bank);
    }

    private User createUser(){
        User user = new User();
        user.setAddress("Address");
        user.setName("Name");
        user.setSurname("Surname");
        user.setPatronymic("Patronymic");
        user.setPhone("Phone");
        return userRepository.save(user);
    }

    private Account createAccount(User user, Bank bank){
        Account account = new Account();
        account.setUser(user);
        account.setBank(bank);
        account.setAccountNumber("12345");
        account.setBalance(BigDecimal.ZERO);
        return account;
    }

    @Test
    public void findByUserId(){
        User user = createUser();
        Account account = createAccount(user, createBank());
        repository.save(account);

        Iterable<Account> findAccounts = repository.findByUserId(user.getId());

        assertThat(findAccounts, containsInAnyOrder(account));
    }

    @Test
    public void findByAccountNumber(){
        Account account = createAccount(createUser(), createBank());
        repository.save(account);

        Account findAccount = repository.findByAccountNumber(account.getAccountNumber()).orElse(null);

        assertNotNull(findAccount);
        assertEquals(account, findAccount);
    }

    @Test
    public void exists(){
        User user = createUser();
        Bank bank = createBank();
        Bank tmpBank = new Bank();
        tmpBank.setId(5);
        Account account = createAccount(user, bank);
        repository.save(account);

        assertTrue(repository.existsByAccountNumber(account.getAccountNumber()));
        assertTrue(repository.existsByUserAndBank(user, bank));
        assertFalse(repository.existsByAccountNumber("12346"));
        assertFalse(repository.existsByUserAndBank(user, tmpBank));
    }
}
