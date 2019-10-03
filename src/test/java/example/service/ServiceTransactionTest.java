package example.service;

import example.entity.Account;
import example.entity.Bank;
import example.entity.Transaction;
import example.entity.User;
import example.exception.BalanceException;
import example.exception.NotFoundException;
import example.exception.ScaleException;
import example.model.SortMode;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServiceTransactionTest extends ServiceTest{
    @Test
    public void addGoodTransaction(){
        Account get = createAccountWithBalance("0.00");
        Account send = createAccountWithBalance("50.02");
        BigDecimal amount = new BigDecimal("10.01");
        when(serviceAccountMock.search("1")).thenReturn(send);
        when(serviceAccountMock.search("2")).thenReturn(get);

        serviceTransaction.add("1", "2", amount);

        Assert.assertEquals(0, send.getBalance().compareTo(new BigDecimal("40.01")));
        Assert.assertEquals(0, get.getBalance().compareTo(new BigDecimal("10.01")));
        verify(transactionRepositoryMock).save(any(Transaction.class));
        verifyNoMoreInteractions(transactionRepositoryMock);
        verify(serviceAccountMock).search("1");
        verify(serviceAccountMock).search("2");
    }

    @Test(expected = ScaleException.class)
    public void addTransactionWithBadScale(){
        when(serviceAccountMock.search("1")).thenReturn(createAccountWithBalance("0.00"));
        when(serviceAccountMock.search("2")).thenReturn(createAccountWithBalance("0.00"));
        BigDecimal amount = new BigDecimal("10.001");

        serviceTransaction.add("1", "2", amount);
    }

    @Test(expected = BalanceException.class)
    public void addTransactionWithBadBalance(){
        when(serviceAccountMock.search("1")).thenReturn(createAccountWithBalance("0.00"));
        when(serviceAccountMock.search("2")).thenReturn(createAccountWithBalance("0.00"));
        BigDecimal amount = new BigDecimal("10.01");

        serviceTransaction.add("1", "2", amount);
    }

    @Test(expected = NotFoundException.class)
    public void addTransactionWithNotFound(){
        serviceTransaction.add("1", "2", new BigDecimal("10.01"));
    }

    @Test
    public void searchAsc(){
        when(serviceAccountMock.search("1")).thenReturn(createAccountWithBalance("0.00"));
        when(serviceAccountMock.search("2")).thenReturn(createAccountWithBalance("0.00"));
        serviceTransaction.search("1", "2", SortMode.ASC);

        verify(transactionRepositoryMock).findByAccountSendAndAccountGetOrderByInstantAsc(any(Account.class), any(Account.class));
        verifyNoMoreInteractions(transactionRepositoryMock);
        verify(serviceAccountMock).search("1");
        verify(serviceAccountMock).search("2");
    }

    @Test
    public void searchDesc(){
        when(serviceAccountMock.search("1")).thenReturn(createAccountWithBalance("0.00"));
        when(serviceAccountMock.search("2")).thenReturn(createAccountWithBalance("0.00"));
        serviceTransaction.search("1", "2", SortMode.DESC);

        verify(transactionRepositoryMock).findByAccountSendAndAccountGetOrderByInstantDesc(any(Account.class), any(Account.class));
        verifyNoMoreInteractions(transactionRepositoryMock);
        verify(serviceAccountMock).search("1");
        verify(serviceAccountMock).search("2");
    }

    @Test(expected = NotFoundException.class)
    public void errorSearch(){
        serviceTransaction.search("1", "2", SortMode.DESC);
    }

    @Test
    public void getTransaction(){
        when(serviceUserMock.search("12")).thenReturn(createUserWithId(12));

        serviceTransaction.getTransaction("12");

        verify(transactionRepositoryMock).findByUser(12);
        verifyNoMoreInteractions(transactionRepositoryMock);
        verify(serviceUserMock).search("12");
    }

    @Test
    public void getTransactionByBank(){
        when(serviceUserMock.search("12")).thenReturn(createUserWithId(12));
        when(serviceBankMock.search("21")).thenReturn(createBankWithId(21));

        serviceTransaction.getTransactionByBank("12", "21");

        verify(transactionRepositoryMock).findByUserAndBank(12, 21);
        verifyNoMoreInteractions(transactionRepositoryMock);
        verify(serviceUserMock).search("12");
        verify(serviceBankMock).search("21");
    }
}
