package example.service;

import example.entity.Bank;
import example.repository.BankRepository;
import example.exception.DuplicateException;
import org.springframework.stereotype.Service;

@Service
public class ServiceBank {
    private final BankRepository repository;

    public ServiceBank(BankRepository repository) {
        this.repository = repository;
    }

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
