package example.controller;

import example.entity.Account;
import example.entity.Bank;
import example.entity.User;
import example.model.AccountModel;
import example.service.ServiceAccount;
import example.service.ServiceBank;
import example.service.ServiceUser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class ControllerAccountTest {
    @Mock
    private ServiceAccount serviceAccount;

    @Mock
    private ServiceUser serviceUser;

    @Mock
    private ServiceBank serviceBank;

    private MockMvc mvc;

    private AccountModel model;

    @Before
    public void setup(){
        serviceAccount = mock(ServiceAccount.class);
        serviceUser = mock(ServiceUser.class);
        serviceBank = mock(ServiceBank.class);
        mvc = MockMvcBuilders.standaloneSetup(new ControllerAccount(serviceAccount, serviceUser, serviceBank)).build();
        model = new AccountModel();
        model.setUserIdOrPhone("1");
        model.setBankIdOrNameOrPhone("3");
    }

    @Test
    public void test() throws Exception{
        User user = new User();
        user.setId(12);
        Bank bank = new Bank();
        bank.setId(21);
        Account account = new Account();
        account.setBank(bank);
        account.setUser(user);
        when(serviceUser.search("1")).thenReturn(user);
        when(serviceBank.search("3")).thenReturn(bank);

        mvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .content(model.toString()))
                .andDo(print());
        verify(serviceUser).search(model.getUserIdOrPhone());
        verify(serviceBank).search(model.getBankIdOrNameOrPhone());
        verify(serviceAccount).add(account.getUser(), account.getBank());
        verifyNoMoreInteractions(serviceAccount, serviceBank, serviceUser);
    }
}
