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

    public Account Add(Account account){
        if(repository.existsByUserAndBank(account.getUser(), account.getBank())){
            throw new DuplicateException("user, bank");
        }
        account.setBalance(BigDecimal.ZERO);
        account.setAccountNumber(GenerateAccountNumber());
        while(repository.existsByAccountNumber(account.getAccountNumber())){
            account.setAccountNumber(GenerateAccountNumber());
        }
        return repository.save(account);
    }

    public Account Search(String idOrAccountNumber){
        if(CheckNumeric(idOrAccountNumber)){
            int id = Integer.parseInt(idOrAccountNumber);
            if(repository.existsById(id)) {
                return repository.findById(id).get();
            }
            return null;
        }
        if(repository.existsByAccountNumber(idOrAccountNumber)){
            return repository.findByAccountNumber(idOrAccountNumber).get();
        }
        return null;
    }

    public Iterable<Account> GetAccountsByUser(User user){
        return repository.findByUserId(user.getId());
    }

    private String GenerateAccountNumber(){
        String res = String.valueOf(RandomInt(9, 1));
        for(int i = 0; i < 9; i++){
            res += String.valueOf(RandomInt(9, 0));
        }
        return res;
    }

    private int RandomInt(int max, int min){
        return (int) (Math.random()*(max - min + 1)) + min;
    }

    private boolean CheckNumeric(String s){
        try{
            Integer.parseInt(s);
            return true;
        }
        catch(NumberFormatException e){
            return false;
        }
    }
}
