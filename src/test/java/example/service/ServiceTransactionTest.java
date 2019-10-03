package example.service;

import example.entity.Account;
import example.entity.Bank;
import example.entity.Transaction;
import example.entity.User;
import example.exception.BalanceException;
import example.exception.ScaleException;
import example.repository.TransactionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServiceTransactionTest {
    private TransactionRepository repositoryMock;

    private ServiceTransaction service;

    @Before
    public void initialization(){
        repositoryMock = mock(TransactionRepository.class);
        service = new ServiceTransaction(repositoryMock, serviceAccount, serviceUser, serviceBank);
    }

    @Test
    public void addGoodTransaction(){
        Account get = new Account();
        get.setBalance(BigDecimal.ZERO);
        Account send = new Account();
        send.setBalance(new BigDecimal("50.02"));
        BigDecimal amount = new BigDecimal("10.01");

        service.add(send, get, amount);

        Assert.assertEquals(0, send.getBalance().compareTo(new BigDecimal("40.01")));
        Assert.assertEquals(0, get.getBalance().compareTo(new BigDecimal("10.01")));
        verify(repositoryMock).save(any(Transaction.class));
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test(expected = ScaleException.class)
    public void addTransactionWithBadScale(){
        BigDecimal amount = new BigDecimal("10.001");

        service.add(null, null, amount);
    }

    @Test(expected = BalanceException.class)
    public void addTransactionWithBadBalance(){
        Account send = new Account();
        send.setBalance(new BigDecimal("10.02"));
        BigDecimal amount = new BigDecimal("10.03");

        service.add(send, null, amount);
    }

    @Test
    public void searchAsc(){
        service.search(null, null, "Asc");

        verify(repositoryMock).findByAccountSendAndAccountGetOrderByDateAsc(null, null);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void searchDesc(){
        service.search(null, null, "Desc");

        verify(repositoryMock).findByAccountSendAndAccountGetOrderByDateDesc(null, null);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void getTransaction(){
        User user = new User();
        user.setId(12);
        service.getTransaction(user);

        verify(repositoryMock).findByUser(12);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void getTransactionByBank(){
        User user = new User();
        user.setId(12);
        Bank bank = new Bank();
        bank.setId(21);
        service.getTransactionByBank(user, bank);

        verify(repositoryMock).findByUserAndBank(12, 21);
        verifyNoMoreInteractions(repositoryMock);
    }
}
