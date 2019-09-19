package example.service;

import example.entity.User;
import example.exception.DuplicateException;
import example.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ServiceUserTest {
    private UserRepository repositoryMock;

    private ServiceUser service;

    @Before
    public void initialization(){
        repositoryMock = mock(UserRepository.class);
        service = new ServiceUser(repositoryMock);
    }

    @Test
    public void add(){
        User user = new User();
        when(repositoryMock.existsByPhone(null)).thenReturn(false);

        service.add(user);
        verify(repositoryMock).existsByPhone(null);
        verify(repositoryMock).save(user);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test(expected = DuplicateException.class)
    public void addDuplicate(){
        User user = new User();
        when(repositoryMock.existsByPhone(null)).thenReturn(true);

        service.add(user);
        verify(repositoryMock).existsByPhone(null);
    }
}
