package example.service;

import example.entity.Bank;
import example.model.BankModel;
import example.repository.BankRepository;
import example.exception.DuplicateException;
import org.springframework.stereotype.Service;

@Service
public class ServiceBank {
    private final BankRepository repository;

    public ServiceBank(BankRepository repository) {
        this.repository = repository;
    }

    public Bank add(BankModel model){
        if(repository.existsByName(model.getName())) {
            throw new DuplicateException("name");
        }
        if(repository.existsByPhone(model.getPhone())) {
            throw new DuplicateException("phone");
        }
        Bank bank = new Bank();
        bank.setName(model.getName());
        bank.setAddress(model.getAddress());
        bank.setPhone(model.getPhone());
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
            return repository.findByName(idOrNameOrPhone).orElse(null);
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
