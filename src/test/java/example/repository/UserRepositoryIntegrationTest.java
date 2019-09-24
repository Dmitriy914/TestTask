package example.repository;

import example.entity.User;
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

    private User createUser(){
        User user = new User();
        user.setAddress("Address");
        user.setName("Name");
        user.setSurname("Surname");
        user.setPatronymic("Patronymic");
        user.setPhone("Phone");
        return user;
    }

    @Test
    public void findByPhone(){
        User user = createUser();
        repository.save(user);

        User saveUser = repository.findByPhone("Phone").orElse(null);

        assertEquals(user, saveUser);
    }

    @Test
    public void existsByPhone(){
        assertFalse(repository.existsByPhone("Phone"));
        repository.save(createUser());
        assertTrue(repository.existsByPhone("Phone"));
    }
}
