package example.service;

import example.entity.User;
import example.exception.DuplicateException;
import example.exception.NotFoundException;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class ServiceUserTest extends ServiceTest{
    @Test
    public void addNotExistingUser(){
        when(userRepositoryMock.existsByPhone("phone")).thenReturn(false);

        serviceUser.add("surname", "name", "patronymic", "address", "phone");
        verify(userRepositoryMock).existsByPhone("phone");
        verify(userRepositoryMock).save(any(User.class));
        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test(expected = DuplicateException.class)
    public void addUserWithExistingPhone(){
        when(userRepositoryMock.existsByPhone("phone")).thenReturn(true);

        serviceUser.add("surname", "name", "patronymic", "address", "phone");
    }

    @Test
    public void searchById(){
        when(userRepositoryMock.findById(12)).thenReturn(Optional.of(new User()));

        serviceUser.search("12");

        verify(userRepositoryMock).findById(12);
        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    public void searchByPhone(){
        when(userRepositoryMock.findByPhone("phone")).thenReturn(Optional.of(new User()));

        serviceUser.search("phone");

        verify(userRepositoryMock).findByPhone("phone");
        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test(expected = NotFoundException.class)
    public void errorSearch(){
        when(userRepositoryMock.findById(12)).thenReturn(Optional.empty());

        serviceUser.search("12");
    }
}
