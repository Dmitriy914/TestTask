package example.service;

import example.entity.Bank;
import example.exception.DuplicateException;
import example.repository.BankRepository;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ServiceBankTest {
    private BankRepository repositoryMock;

    private ServiceBank service;

    @Before
    public void initialization(){
        repositoryMock = mock(BankRepository.class);
        service = new ServiceBank(repositoryMock);
    }

    @Test
    public void addNotExistingBank() {
        Bank bank = new Bank();
        bank.setName("name");
        bank.setPhone("phone");
        when(repositoryMock.existsByName("name")).thenReturn(false);
        when(repositoryMock.existsByPhone("phone")).thenReturn(false);

        service.add(bank);
        verify(repositoryMock).existsByName("name");
        verify(repositoryMock).existsByPhone("phone");
        verify(repositoryMock).save(bank);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test(expected = DuplicateException.class)
    public void addBankWithExistingName(){
        Bank bank = new Bank();
        bank.setName("name");
        when(repositoryMock.existsByName("name")).thenReturn(true);

        service.add(bank);
    }

    @Test(expected = DuplicateException.class)
    public void addBankWithExistingPhone(){
        Bank bank = new Bank();
        bank.setName("name");
        bank.setPhone("phone");
        when(repositoryMock.existsByName("name")).thenReturn(false);
        when(repositoryMock.existsByPhone("phone")).thenReturn(true);

        service.add(bank);
    }

    @Test
    public void searchAll() {
        service.searchAll();

        verify(repositoryMock).findAll();
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void searchById() {
        service.search("12");

        verify(repositoryMock).findById(12);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void searchByName(){
        when(repositoryMock.existsByName("name")).thenReturn(true);

        service.search("name");

        verify(repositoryMock).existsByName("name");
        verify(repositoryMock).findByName("name");
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void searchByPhone(){
        when(repositoryMock.existsByName("phone")).thenReturn(false);

        service.search("phone");

        verify(repositoryMock).existsByName("phone");
        verify(repositoryMock).findByPhone("phone");
        verifyNoMoreInteractions(repositoryMock);
    }
}
