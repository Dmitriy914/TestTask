package example.service;

import example.entity.Bank;
import example.exception.NotFoundException;
import example.repository.BankRepository;
import example.exception.DuplicateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceBank {
    private final BankRepository repository;

    public ServiceBank(BankRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Bank add(String name, String address, String phone){
        if(repository.existsByName(name)) {
            throw new DuplicateException("name");
        }
        if(repository.existsByPhone(phone)) {
            throw new DuplicateException("phone");
        }
        Bank bank = new Bank();
        bank.setName(name);
        bank.setAddress(address);
        bank.setPhone(phone);
        return repository.save(bank);
    }

    public Iterable<Bank> searchAll(){
        return repository.findAll();
    }

    public Bank search(String idOrNameOrPhone){
        Bank bank;
        if(checkNumeric((idOrNameOrPhone))) bank = repository.findById(Integer.parseInt(idOrNameOrPhone)).orElse(null);
        else bank = repository.findByName(idOrNameOrPhone).orElse(null);
        if(bank == null) bank = repository.findByPhone(idOrNameOrPhone).orElse(null);

        if(bank == null) throw new NotFoundException("Bank");
        else return bank;
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
