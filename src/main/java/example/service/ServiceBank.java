package example.service;

import example.entity.Bank;
import example.repository.BankRepository;
import example.exception.DuplicateException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ServiceBank {
    @Autowired
    private BankRepository repository;

    public Bank add(Bank bank){
        if(repository.existsByName(bank.getName())) {
            throw new DuplicateException("name");
        }
        if(repository.existsByPhone(bank.getPhone())) {
            throw new DuplicateException("phone");
        }
        return repository.save(bank);
    }

    public Iterable<Bank> searchAll(){
        return repository.findAll();
    }

    public Bank search(String idOrNameOrPhone){
        if(checkNumeric((idOrNameOrPhone))){
            return repository.findById(Integer.parseInt(idOrNameOrPhone)).orElse(null);
        }
        if(repository.existsByName(idOrNameOrPhone)){
            return repository.findByName(idOrNameOrPhone).get();
        }
        return repository.findByPhone(idOrNameOrPhone).orElse(null);
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
