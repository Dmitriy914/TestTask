package example.service;

import example.entity.Account;
import example.entity.Bank;
import example.entity.User;
import example.exception.DuplicateException;
import example.repository.AccountRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ServiceAccount {
    private final AccountRepository repository;
    private final ServiceUser serviceUser;
    private final ServiceBank serviceBank;

    public ServiceAccount(AccountRepository repository, ServiceUser serviceUser, ServiceBank serviceBank) {
        this.repository = repository;
        this.serviceUser = serviceUser;
        this.serviceBank = serviceBank;
    }

    @Transactional
    public Account add(String userIdOrPhone, String bankIdOrNameOrPhone){
        User user = serviceUser.search(userIdOrPhone);
        Bank bank = serviceBank.search(bankIdOrNameOrPhone);
        if(repository.existsByUserAndBank(user, bank)){
            throw new DuplicateException("user, bank");
        }
        Account account = new Account();
        account.setUser(user);
        account.setBank(bank);
        account.setBalance(BigDecimal.ZERO);
        account.setAccountNumber(generateAccountNumber());
        return repository.save(account);
    }

    public Account search(String idOrAccountNumber){
        Account account = null;
        if(checkNumeric(idOrAccountNumber)) account = repository.findById(Integer.parseInt(idOrAccountNumber)).orElse(null);
        if(account == null) account = repository.findByAccountNumber(idOrAccountNumber).orElse(null);
        return account;
    }

    @Transactional
    public Iterable<Account> getAccountsByUser(String user){
        return repository.findByUserId(serviceUser.search(user).getId());
    }

    private String generateAccountNumber(){
        String res = RandomStringUtils.randomNumeric(10);
        if(repository.existsByAccountNumber(res)) return generateAccountNumber();
        else return res;
    }

    private boolean checkNumeric(String s){
        try{
            Integer.parseInt(s);
            return true;
        }
        catch(NumberFormatException e){
            return false;
        }
    }
}
