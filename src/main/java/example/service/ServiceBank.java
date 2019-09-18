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

    public Bank Add(Bank bank){
        if(repository.existsByName(bank.getName())) {
            throw new DuplicateException("name");
        }
        if(repository.existsByPhone(bank.getPhone())) {
            throw new DuplicateException("phone");
        }
        return repository.save(bank);
    }

    public Iterable<Bank> SearchAll(){
        return repository.findAll();
    }

    public Bank Search(String idOrNameOrPhone){
        if(CheckNumeric((idOrNameOrPhone))){
            int id = Integer.parseInt(idOrNameOrPhone);
            if(repository.existsById(id)){
                return repository.findById(id).get();
            }
            return null;
        }
        if(repository.existsByName(idOrNameOrPhone)){
            return repository.findByName(idOrNameOrPhone).get();
        }
        if(repository.existsByPhone(idOrNameOrPhone)){
            return repository.findByPhone(idOrNameOrPhone).get();
        }
        return null;
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
