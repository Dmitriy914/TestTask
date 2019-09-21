package example.service;

import example.entity.Account;
import example.entity.Bank;
import example.entity.User;
import example.exception.DuplicateException;
import example.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
        account.setBalance(BigDecimal.ZERO);
        account.setAccountNumber(generateAccountNumber());
        while(repository.existsByAccountNumber(account.getAccountNumber())){
            account.setAccountNumber(generateAccountNumber());
        }
        return repository.save(account);
    }

    public Account search(String idOrAccountNumber){
        if(checkNumeric(idOrAccountNumber)){
            int id = Integer.parseInt(idOrAccountNumber);
            if(repository.existsById(id)){
                return repository.findById(id).get();
            }
        }
        return repository.findByAccountNumber(idOrAccountNumber).orElse(null);
    }

    public Iterable<Account> getAccountsByUser(User user){
        return repository.findByUserId(user.getId());
    }

    private String generateAccountNumber(){
        String res = String.valueOf(randomInt(9, 1));
        for(int i = 0; i < 9; i++){
            res += String.valueOf(randomInt(9, 0));
        }
        return res;
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
