package example.service;

import example.entity.Account;
import example.exception.DuplicateException;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ServiceAccountTest extends ServiceTest{
    @Test
    public void addNotExistingAccount() {
        when(accountRepositoryMock.existsByUserAndBank(null, null)).thenReturn(false);
        when(accountRepositoryMock.existsByAccountNumber(anyString())).thenReturn(false);

        serviceAccount.add("1", "2");

        verify(accountRepositoryMock).existsByUserAndBank(null, null);
        verify(accountRepositoryMock).existsByAccountNumber(anyString());
        verify(accountRepositoryMock).save(any(Account.class));
        verifyNoMoreInteractions(accountRepositoryMock);
        verify(serviceUserMock).search("1");
        verify(serviceBankMock).search("2");
    }

    @Test(expected = DuplicateException.class)
    public void addExistingAccount(){
        when(accountRepositoryMock.existsByUserAndBank(null, null)).thenReturn(true);

        serviceAccount.add("1", "2");
    }

    @Test
    public void searchById() {
        when(accountRepositoryMock.findById(12)).thenReturn(Optional.of(new Account()));

        serviceAccount.search("12");

        verify(accountRepositoryMock).findById(12);
        verifyNoMoreInteractions(accountRepositoryMock);
    }

    @Test
    public void searchByAccountNumber(){
        when(accountRepositoryMock.findById(123)).thenReturn(Optional.empty());

        serviceAccount.search("123");

        verify(accountRepositoryMock).findById(123);
        verify(accountRepositoryMock).findByAccountNumber("123");
        verifyNoMoreInteractions(accountRepositoryMock);
    }

    @Test
    public void getAccountsByUser() {
        when(serviceUserMock.search("12")).thenReturn(createUserWithId(12));

        serviceAccount.getAccountsByUser("12");

        verify(accountRepositoryMock).findByUserId(12);
        verifyNoMoreInteractions(accountRepositoryMock);
        verify(serviceUserMock).search("12");
    }
}
