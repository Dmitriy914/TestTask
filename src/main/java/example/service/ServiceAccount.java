package example.service;

import example.entity.Account;
import example.entity.User;
import example.exception.DuplicateException;
import example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ServiceAccount {
    @Autowired
    private AccountRepository repository;

    public Account add(Account account){
        if(repository.existsByUserAndBank(account.getUser(), account.getBank())){
            throw new DuplicateException("user, bank");
        }
        account.setBalance(BigDecimal.ZERO);
        account.setAccountNumber(generateAccountNumber());
        while(repository.existsByAccountNumber(account.getAccountNumber())){
            account.setAccountNumber(generateAccountNumber());
        }
        return repository.save(account);
    }

    public Account search(String idOrAccountNumber){
        if(checkNumeric(idOrAccountNumber)){
            return repository.findById(Integer.parseInt(idOrAccountNumber)).orElse(null);
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
