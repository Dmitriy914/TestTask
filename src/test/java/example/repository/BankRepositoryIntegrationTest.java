package example.repository;

import example.entity.Bank;
import org.junit.Test;

import static org.junit.Assert.*;

public class BankRepositoryIntegrationTest extends RepositoryTest{
    @Test
    public void findByName(){
        Bank bank = createAndSaveBankRandom();

        Bank foundBank = bankRepository.findByName(bank.getName()).orElse(null);

        assertEquals(bank, foundBank);
    }

    @Test
    public void findByPhone(){
        Bank bank = createAndSaveBankRandom();

        Bank foundBank = bankRepository.findByPhone(bank.getPhone()).orElse(null);

        assertEquals(bank, foundBank);
    }

    @Test
    public void exists(){
        Bank bank = createAndSaveBankRandom();

        assertTrue(bankRepository.existsByName(bank.getName()));
        assertTrue(bankRepository.existsByPhone(bank.getPhone()));
        assertFalse(bankRepository.existsByPhone("NotExistsPhone"));
        assertFalse(bankRepository.existsByName("NotExistsName"));
    }
}
