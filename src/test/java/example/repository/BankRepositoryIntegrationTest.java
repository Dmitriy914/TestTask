package example.repository;

import example.entity.Bank;
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

    private Bank createBank(){
        Bank bank = new Bank();
        bank.setName("Name");
        bank.setAddress("Address");
        bank.setPhone("Phone");
        return bank;
    }

    @Test
    public void findByName(){
        Bank bank = createBank();
        repository.save(bank);

        Bank findBank = repository.findByName(bank.getName()).orElse(null);

        assertNotNull(findBank);
        assertEquals(bank, findBank);
    }

    @Test
    public void findByPhone(){
        Bank bank = createBank();
        repository.save(bank);

        Bank findBank = repository.findByPhone(bank.getPhone()).orElse(null);

        assertNotNull(findBank);
        assertEquals(bank, findBank);
    }

    @Test
    public void exists(){
        Bank bank = createBank();
        repository.save(bank);

        assertTrue(repository.existsByName(bank.getName()));
        assertTrue(repository.existsByPhone(bank.getPhone()));
        assertFalse(repository.existsByPhone("Phone1"));
        assertFalse(repository.existsByName("Name1"));
    }
}
