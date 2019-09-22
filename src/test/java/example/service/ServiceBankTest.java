package example.service;

import example.entity.Bank;
import example.exception.DuplicateException;
import example.model.BankModel;
import example.repository.BankRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

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
        BankModel model = new BankModel();
        model.setName("name");
        model.setPhone("phone");
        when(repositoryMock.existsByName("name")).thenReturn(false);
        when(repositoryMock.existsByPhone("phone")).thenReturn(false);

        service.add(model);
        verify(repositoryMock).existsByName("name");
        verify(repositoryMock).existsByPhone("phone");
        verify(repositoryMock).save(any(Bank.class));
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test(expected = DuplicateException.class)
    public void addBankWithExistingName(){
        BankModel model = new BankModel();
        model.setName("name");
        when(repositoryMock.existsByName("name")).thenReturn(true);

        service.add(model);
    }

    @Test(expected = DuplicateException.class)
    public void addBankWithExistingPhone(){
        BankModel model = new BankModel();
        model.setName("name");
        model.setPhone("phone");
        when(repositoryMock.existsByName("name")).thenReturn(false);
        when(repositoryMock.existsByPhone("phone")).thenReturn(true);

        service.add(model);
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
        when(repositoryMock.findByName("name")).thenReturn(Optional.of(new Bank()));

        service.search("name");

        verify(repositoryMock).findByName("name");
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void searchByPhone(){
        when(repositoryMock.findByName("phone")).thenReturn(Optional.empty());

        service.search("phone");

        verify(repositoryMock).findByName("phone");
        verify(repositoryMock).findByPhone("phone");
        verifyNoMoreInteractions(repositoryMock);
    }
}
