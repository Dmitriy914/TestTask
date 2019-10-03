package example.service;

import example.entity.Account;
import example.entity.Bank;
import example.entity.User;
import example.exception.DuplicateException;
import example.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ServiceAccountTest {
    private AccountRepository repositoryMock;

    private ServiceAccount service;

    @Before
    public void initialization(){
        repositoryMock = mock(AccountRepository.class);
        service = new ServiceAccount(repositoryMock, serviceUser, serviceBank);
    }

    @Test
    public void addNotExistingAccount() {
        User user = new User();
        Bank bank = new Bank();
        when(repositoryMock.existsByUserAndBank(user, bank)).thenReturn(false);
        when(repositoryMock.existsByAccountNumber(anyString())).thenReturn(false);

        service.add(user, bank);

        verify(repositoryMock).existsByUserAndBank(user, bank);
        verify(repositoryMock).existsByAccountNumber(anyString());
        verify(repositoryMock).save(any(Account.class));
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test(expected = DuplicateException.class)
    public void addExistingAccount(){
        User user = new User();
        Bank bank = new Bank();
        when(repositoryMock.existsByUserAndBank(user, bank)).thenReturn(true);

        service.add(user, bank);
    }

    @Test
    public void searchById() {
        when(repositoryMock.findById(12)).thenReturn(Optional.of(new Account()));

        service.search("12");

        verify(repositoryMock).findById(12);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void searchByAccountNumber(){
        when(repositoryMock.findById(123)).thenReturn(Optional.empty());

        service.search("123");

        verify(repositoryMock).findById(123);
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
