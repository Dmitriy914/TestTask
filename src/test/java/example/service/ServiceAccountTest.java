package example.service;

import example.entity.Account;
import example.entity.Bank;
import example.entity.User;
import example.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

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
    public void add() {
        Account account = new Account();
        Bank bank = new Bank();
        User user = new User();
        when(repositoryMock.existsByUserAndBank(user, bank)).thenReturn(false);
        when(repositoryMock.existsByAccountNumber(account.getAccountNumber())).thenReturn(false);

        service.add(account);

        verify(repositoryMock).existsByUserAndBank(user, bank);
        verify(repositoryMock).findByAccountNumber(ArgumentMatchers.anyString());
        verify(repositoryMock).save(account);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void search() {
    }

    @Test
    public void getAccountsByUser() {
    }
}
