package example.service;

import example.entity.User;
import example.exception.DuplicateException;
import example.model.UserModel;
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
    public void addNotExistingUser(){
        UserModel model = new UserModel();
        model.setPhone("phone");
        when(repositoryMock.existsByPhone("phone")).thenReturn(false);

        service.add(model);
        verify(repositoryMock).existsByPhone("phone");
        verify(repositoryMock).save(any(User.class));
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test(expected = DuplicateException.class)
    public void addUserWithExistingPhone(){
        UserModel model = new UserModel();
        model.setPhone("phone");
        when(repositoryMock.existsByPhone("phone")).thenReturn(true);

        service.add(model);
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
