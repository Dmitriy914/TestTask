package example.repository;

import example.entity.Account;
import example.entity.Bank;
import example.entity.User;
import org.junit.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

public class AccountRepositoryIntegrationTest extends SuperRepositoryTest{
    @Test
    public void findByUserId(){
        User user = createAndSaveUserRandom();
        Account account1 = createAndSaveAccountRandom(user, createAndSaveBankRandom());
        Account account2 = createAndSaveAccountRandom(user, createAndSaveBankRandom());

        Iterable<Account> foundAccounts = accountRepository.findByUserId(user.getId());

        assertThat(foundAccounts, containsInAnyOrder(account1, account2));
    }

    @Test
    public void findByAccountNumber(){
        Account account = createAndSaveAccountRandom(createAndSaveUserRandom(), createAndSaveBankRandom());

        Account foundAccount = accountRepository.findByAccountNumber(account.getAccountNumber()).orElse(null);

        assertEquals(account, foundAccount);
    }

    @Test
    public void exists(){
        User user = createAndSaveUserRandom();
        Bank userBank = createAndSaveBankRandom();
        Bank freeBank = createAndSaveBankRandom();
        Account account = createAndSaveAccountRandom(user, userBank);

        assertTrue(accountRepository.existsByAccountNumber(account.getAccountNumber()));
        assertTrue(accountRepository.existsByUserAndBank(user, userBank));
        assertFalse(accountRepository.existsByAccountNumber("NotExistsAccountNumber"));
        assertFalse(accountRepository.existsByUserAndBank(user, freeBank));
    }
}
