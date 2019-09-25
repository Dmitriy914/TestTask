package example.repository;

import example.entity.Bank;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BankRepositoryIntegrationTest {
    @Autowired
    private BankRepository repository;

    private Bank createAndSaveBankRandom(){
        Bank bank = new Bank();
        bank.setName(RandomStringUtils.randomAlphabetic(10));
        bank.setAddress(RandomStringUtils.randomAlphabetic(10));
        bank.setPhone(RandomStringUtils.randomAlphabetic(10));
        return repository.save(bank);
    }

    @Test
    public void findByName(){
        Bank bank = createAndSaveBankRandom();

        Bank foundBank = repository.findByName(bank.getName()).orElse(null);

        assertEquals(bank, foundBank);
    }

    @Test
    public void findByPhone(){
        Bank bank = createAndSaveBankRandom();

        Bank foundBank = repository.findByPhone(bank.getPhone()).orElse(null);

        assertEquals(bank, foundBank);
    }

    @Test
    public void exists(){
        Bank bank = createAndSaveBankRandom();

        assertTrue(repository.existsByName(bank.getName()));
        assertTrue(repository.existsByPhone(bank.getPhone()));
        assertFalse(repository.existsByPhone("NotExistsPhone"));
        assertFalse(repository.existsByName("NotExistsName"));
    }
}
