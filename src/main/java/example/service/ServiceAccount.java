package example.service;

import example.entity.Account;
import example.entity.Bank;
import example.entity.User;
import example.exception.DuplicateException;
import example.repository.AccountRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ServiceAccount {
    private final AccountRepository repository;

    public ServiceAccount(AccountRepository repository) {
        this.repository = repository;
    }

    public Account add(User user, Bank bank){
        if(repository.existsByUserAndBank(user, bank)){
            throw new DuplicateException("user, bank");
        }
        Account account = new Account();
        account.setUser(user);
        account.setBank(bank);
        account.setBalance(BigDecimal.ZERO.setScale(2));
        account.setAccountNumber(generateAccountNumber());
        return repository.save(account);
    }

    public Account search(String idOrAccountNumber){
        if(checkNumeric(idOrAccountNumber)){
            Optional<Account> account = repository.findById(Integer.parseInt(idOrAccountNumber));
            if(account.isPresent()) return account.get();
        }
        return repository.findByAccountNumber(idOrAccountNumber).orElse(null);
    }

    public Iterable<Account> getAccountsByUser(User user){
        return repository.findByUserId(user.getId());
    }

    private String generateAccountNumber(){
        String res = RandomStringUtils.randomNumeric(10);
        if(repository.existsByAccountNumber(res)) return generateAccountNumber();
        else return res;
    }

    private int randomInt(int max, int min){
        return (int) (Math.random()*(max - min + 1)) + min;
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
