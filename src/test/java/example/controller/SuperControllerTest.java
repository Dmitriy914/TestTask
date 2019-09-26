package example.controller;

import example.entity.Account;
import example.entity.Bank;
import example.entity.Transaction;
import example.entity.User;
import example.model.BankModel;
import example.model.TransactionModel;
import example.model.UserModel;
import example.repository.AccountRepository;
import example.repository.BankRepository;
import example.repository.TransactionRepository;
import example.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class SuperControllerTest {
    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BankRepository bankRepository;

    @Autowired
    TransactionRepository transactionRepository;



    UserModel createUserModelRandom(){
        UserModel model = new UserModel();
        model.setName(randomAlphabetic(10));
        model.setPhone(randomAlphabetic(10));
        model.setAddress(randomAlphabetic(10));
        model.setPatronymic(randomAlphabetic(10));
        model.setSurname(randomAlphabetic(10));
        return model;
    }

    BankModel createBankModelRandom(){
        BankModel model = new BankModel();
        model.setName(randomAlphabetic(10));
        model.setAddress(randomAlphabetic(10));
        model.setPhone(randomAlphabetic(10));
        return model;
    }

    TransactionModel createTransactionModel(Account send, Account get, String amount){
        return new TransactionModel(send.getAccountNumber(), get.getAccountNumber(), new BigDecimal(amount));
    }



    User createAndSaveUserRandom(){
        User user = new User();
        user.setSurname(randomAlphabetic(10));
        user.setPhone(randomAlphabetic(10));
        user.setAddress(randomAlphabetic(10));
        user.setName(randomAlphabetic(10));
        user.setPatronymic(randomAlphabetic(10));
        return userRepository.save(user);
    }

    Bank createAndSaveBankRandom(){
        Bank bank = new Bank();
        bank.setName(randomAlphabetic(10));
        bank.setAddress(randomAlphabetic(10));
        bank.setPhone(randomAlphabetic(10));
        return bankRepository.save(bank);
    }

    Account createAndSaveAccountRandom(User user, Bank bank){
        Account account = new Account();
        account.setUser(user);
        account.setBank(bank);
        account.setBalance(new BigDecimal("100.00"));
        account.setAccountNumber(RandomStringUtils.randomNumeric(10));
        return accountRepository.save(account);
    }

    Transaction createAndSaveTransaction(Account send, Account get, String amount, long millisec){
        Transaction transaction = new Transaction();
        transaction.setAccountSend(send);
        transaction.setAccountGet(get);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setDate(new Date(millisec));
        return transactionRepository.save(transaction);
    }
}
