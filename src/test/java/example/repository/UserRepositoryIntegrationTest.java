package example.repository;

import example.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryIntegrationTest {
    @Autowired
    private UserRepository repository;

    private User createUserRandom(){
        User user = new User();
        user.setAddress(RandomStringUtils.randomAlphabetic(10));
        user.setName(RandomStringUtils.randomAlphabetic(10));
        user.setSurname(RandomStringUtils.randomAlphabetic(10));
        user.setPatronymic(RandomStringUtils.randomAlphabetic(10));
        user.setPhone(RandomStringUtils.randomAlphabetic(10));
        return repository.save(user);
    }

    @Test
    public void findByPhone(){
        User user = createUserRandom();

        User saveUser = repository.findByPhone(user.getPhone()).orElse(null);

        assertEquals(user, saveUser);
    }

    @Test
    public void existsByPhone(){
        User user = createUserRandom();

        assertTrue(repository.existsByPhone(user.getPhone()));
    }
}
