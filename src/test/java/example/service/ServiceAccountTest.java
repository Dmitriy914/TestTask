package example.service;

import example.entity.Account;
import example.entity.Bank;
import example.entity.User;
import example.exception.DuplicateException;
import example.repository.AccountRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

public class ServiceAccountTest {
    private AccountRepository repositoryMock;

    private ServiceAccount service;

    @Before
    public void initialization(){
        repositoryMock = mock(AccountRepository.class);
        service = new ServiceAccount(repositoryMock);
    }

    @Test
    public void addNotExistingAccount() {
        Account account = new Account();
        account.setUser(new User());
        account.setBank(new Bank());
        when(repositoryMock.existsByUserAndBank(account.getUser(), account.getBank())).thenReturn(false);
        when(repositoryMock.existsByAccountNumber(ArgumentMatchers.anyString())).thenReturn(false);

        service.add(account);

        Assert.assertEquals(0, account.getBalance().compareTo(BigDecimal.ZERO));
        Assert.assertNotNull(account.getAccountNumber());
        verify(repositoryMock).existsByUserAndBank(account.getUser(), account.getBank());
        verify(repositoryMock).existsByAccountNumber(ArgumentMatchers.anyString());
        verify(repositoryMock).save(account);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test(expected = DuplicateException.class)
    public void addExistingAccount(){
        Account account = new Account();
        account.setUser(new User());
        account.setBank(new Bank());
        when(repositoryMock.existsByUserAndBank(account.getUser(), account.getBank())).thenReturn(true);

        service.add(account);
    }

    @Test
    public void searchById() {
        when(repositoryMock.existsById(12)).thenReturn(true);

        service.search("12");

        verify(repositoryMock).existsById(12);
        verify(repositoryMock).findById(12);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void searchByAccountNumber(){
        when(repositoryMock.existsById(123)).thenReturn(false);

        service.search("123");

        verify(repositoryMock).existsById(123);
        verify(repositoryMock).findByAccountNumber("123");
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void getAccountsByUser() {
        User user = new User();
        user.setId(12);

        service.getAccountsByUser(user);

        verify(repositoryMock).findByUserId(12);
        verifyNoMoreInteractions(repositoryMock);
    }
}
