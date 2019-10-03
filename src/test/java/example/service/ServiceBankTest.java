package example.service;

import example.entity.Bank;
import example.exception.DuplicateException;
import example.exception.NotFoundException;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class ServiceBankTest extends ServiceTest{
    @Test
    public void addNotExistingBank() {
        when(bankRepositoryMock.existsByName("name")).thenReturn(false);
        when(bankRepositoryMock.existsByPhone("phone")).thenReturn(false);

        serviceBank.add("name", "address", "phone");
        verify(bankRepositoryMock).existsByName("name");
        verify(bankRepositoryMock).existsByPhone("phone");
        verify(bankRepositoryMock).save(any(Bank.class));
        verifyNoMoreInteractions(bankRepositoryMock);
    }

    @Test(expected = DuplicateException.class)
    public void addBankWithExistingName(){
        when(bankRepositoryMock.existsByName("name")).thenReturn(true);

        serviceBank.add("name", "address", "phone");
    }

    @Test(expected = DuplicateException.class)
    public void addBankWithExistingPhone(){
        when(bankRepositoryMock.existsByName("name")).thenReturn(false);
        when(bankRepositoryMock.existsByPhone("phone")).thenReturn(true);

        serviceBank.add("name", "address", "phone");
    }

    @Test
    public void searchAll() {
        serviceBank.searchAll();

        verify(bankRepositoryMock).findAll();
        verifyNoMoreInteractions(bankRepositoryMock);
    }

    @Test
    public void searchById() {
        when(bankRepositoryMock.findById(12)).thenReturn(Optional.of(new Bank()));
        serviceBank.search("12");

        verify(bankRepositoryMock).findById(12);
        verifyNoMoreInteractions(bankRepositoryMock);
    }

    @Test
    public void searchByName(){
        when(bankRepositoryMock.findByName("name")).thenReturn(Optional.of(new Bank()));

        serviceBank.search("name");

        verify(bankRepositoryMock).findByName("name");
        verifyNoMoreInteractions(bankRepositoryMock);
    }

    @Test
    public void searchByPhone(){
        when(bankRepositoryMock.findByName("phone")).thenReturn(Optional.empty());
        when(bankRepositoryMock.findByPhone("phone")).thenReturn(Optional.of(new Bank()));

        serviceBank.search("phone");

        verify(bankRepositoryMock).findByName("phone");
        verify(bankRepositoryMock).findByPhone("phone");
        verifyNoMoreInteractions(bankRepositoryMock);
    }

    @Test(expected = NotFoundException.class)
    public void errorSearch(){
        when(bankRepositoryMock.findByName("phone")).thenReturn(Optional.empty());
        when(bankRepositoryMock.findByPhone("phone")).thenReturn(Optional.empty());

        serviceBank.search("phone");
    }
}
