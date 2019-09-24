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

    private Bank createBankRandom(){
        Bank bank = new Bank();
        bank.setName(RandomStringUtils.randomAlphabetic(10));
        bank.setAddress(RandomStringUtils.randomAlphabetic(10));
        bank.setPhone(RandomStringUtils.randomAlphabetic(10));
        return repository.save(bank);
    }

    @Test
    public void findByName(){
        Bank bank = createBankRandom();

        Bank findBank = repository.findByName(bank.getName()).orElse(null);

        assertNotNull(findBank);
        assertEquals(bank, findBank);
    }

    @Test
    public void findByPhone(){
        Bank bank = createBankRandom();

        Bank findBank = repository.findByPhone(bank.getPhone()).orElse(null);

        assertNotNull(findBank);
        assertEquals(bank, findBank);
    }

    @Test
    public void exists(){
        Bank bank = createBankRandom();

        assertTrue(repository.existsByName(bank.getName()));
        assertTrue(repository.existsByPhone(bank.getPhone()));
        assertFalse(repository.existsByPhone("Phone1"));
        assertFalse(repository.existsByName("Name1"));
    }
}
