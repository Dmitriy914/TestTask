package example.service;

import example.entity.User;
import example.exception.DuplicateException;
import example.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ServiceUser {
    private final UserRepository repository;

    public ServiceUser(UserRepository repository) {
        this.repository = repository;
    }

    public User add(User user){
        if(repository.existsByPhone(user.getPhone())){
            throw new DuplicateException("phone");
        }
        return repository.save(user);
    }

    public User search(String idOrPhone){
        if(checkNumeric((idOrPhone))){
            return repository.findById(Integer.parseInt(idOrPhone)).orElse(null);
        }
        return repository.findByPhone(idOrPhone).orElse(null);
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
