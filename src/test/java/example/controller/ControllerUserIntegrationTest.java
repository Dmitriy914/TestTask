package example.controller;

import example.entity.User;
import example.model.UserModel;
import example.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class ControllerUserIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository repository;

    @Before
    public void initialization(){
        repository.deleteAll();
    }

    private UserModel createUserModelRandom(){
        UserModel model = new UserModel();
        model.setName(RandomStringUtils.randomAlphabetic(10));
        model.setPhone(RandomStringUtils.randomAlphabetic(10));
        model.setAddress(RandomStringUtils.randomAlphabetic(10));
        model.setPatronymic(RandomStringUtils.randomAlphabetic(10));
        model.setSurname(RandomStringUtils.randomAlphabetic(10));
        return model;
    }

    private User createUserRandom(){
        User user = new User();
        user.setName(RandomStringUtils.randomAlphabetic(10));
        user.setSurname(RandomStringUtils.randomAlphabetic(10));
        user.setAddress(RandomStringUtils.randomAlphabetic(10));
        user.setPhone(RandomStringUtils.randomAlphabetic(10));
        user.setPatronymic(RandomStringUtils.randomAlphabetic(10));
        return repository.save(user);
    }

    @Test
    public void saveUserWithStatusOk(){
        UserModel model = createUserModelRandom();

        ResponseEntity<User> response = restTemplate.postForEntity("http://localhost:" + port + "/users", model, User.class);

        User responseBody = response.getBody();
        User saveUser = repository.findByPhone(model.getPhone()).orElse(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(saveUser, responseBody);
    }
}
