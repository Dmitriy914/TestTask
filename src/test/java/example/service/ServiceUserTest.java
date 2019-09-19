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
        user.setPhone("phone");
        when(repositoryMock.existsByPhone("phone")).thenReturn(false);

        service.add(user);
        verify(repositoryMock).existsByPhone("phone");
        verify(repositoryMock).save(user);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test(expected = DuplicateException.class)
    public void addException(){
        User user = new User();
        user.setPhone("phone");
        when(repositoryMock.existsByPhone("phone")).thenReturn(true);

        service.add(user);
    }

    @Test
    public void searchById(){
        service.search("12");

        verify(repositoryMock).findById(12);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void searchByPhone(){
        service.search("phone");

        verify(repositoryMock).findByPhone("phone");
        verifyNoMoreInteractions(repositoryMock);
    }
}
