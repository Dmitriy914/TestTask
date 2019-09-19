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
    public void add() {
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
    public void addDuplicateName(){
        Bank bank = new Bank();
        bank.setName("name");
        when(repositoryMock.existsByName("name")).thenReturn(true);

        service.add(bank);
    }

    //Test(expected = DuplicateException.class)
    //public void addDuplicatePhone(){

    //}

    @Test
    public void searchAll() {
    }

    @Test
    public void search() {
    }
}
