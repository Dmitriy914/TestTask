package example.service;

import example.entity.Account;
import example.entity.Bank;
import example.entity.User;
import example.repository.AccountRepository;
import example.repository.BankRepository;
import example.repository.TransactionRepository;
import example.repository.UserRepository;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
abstract class ServiceTest {
    final TransactionRepository transactionRepositoryMock;
    final UserRepository userRepositoryMock;
    final BankRepository bankRepositoryMock;
    final AccountRepository accountRepositoryMock;

    final ServiceAccount serviceAccountMock;
    final ServiceUser serviceUserMock;
    final ServiceBank serviceBankMock;

    final ServiceAccount serviceAccount;
    final ServiceUser serviceUser;
    final ServiceBank serviceBank;
    final ServiceTransaction serviceTransaction;

    ServiceTest(){
        transactionRepositoryMock = mock(TransactionRepository.class);
        userRepositoryMock = mock(UserRepository.class);
        bankRepositoryMock = mock(BankRepository.class);
        accountRepositoryMock = mock(AccountRepository.class);

        serviceUserMock= mock(ServiceUser.class);
        serviceAccountMock = mock(ServiceAccount.class);
        serviceBankMock = mock(ServiceBank.class);

        serviceAccount = new ServiceAccount(accountRepositoryMock, serviceUserMock, serviceBankMock);
        serviceTransaction = new ServiceTransaction(transactionRepositoryMock, serviceAccountMock, serviceUserMock, serviceBankMock);
        serviceBank = new ServiceBank(bankRepositoryMock);
        serviceUser = new ServiceUser(userRepositoryMock);
    }

    Account createAccountWithBalance(String balance){
        Account account = new Account();
        account.setBalance(new BigDecimal(balance));
        return account;
    }

    User createUserWithId(Integer id){
        User user = new User();
        user.setId(id);
        return user;
    }

    Bank createBankWithId(Integer id){
        Bank bank = new Bank();
        bank.setId(id);
        return bank;
    }
}
