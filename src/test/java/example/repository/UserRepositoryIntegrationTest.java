package example.repository;

import example.entity.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserRepositoryIntegrationTest extends SuperRepositoryTest{
    @Test
    public void findByPhone(){
        User user = createAndSaveUserRandom();

        User foundUser = userRepository.findByPhone(user.getPhone()).orElse(null);

        assertEquals(user, foundUser);
    }

    @Test
    public void existsByPhone(){
        User user = createAndSaveUserRandom();

        assertTrue(userRepository.existsByPhone(user.getPhone()));
        assertFalse(userRepository.existsByPhone("NotExistsPhone"));
    }
}
