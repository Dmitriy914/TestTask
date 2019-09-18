package example.service;

import example.entity.User;
import example.exception.DuplicateException;
import example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceUser {
    @Autowired
    private UserRepository repository;

    public User Add(User user){
        if(repository.existsByPhone(user.getPhone())){
            throw new DuplicateException("phone");
        }
        return repository.save(user);
    }

    public User Search(String idOrPhone){
        if(CheckNumeric((idOrPhone))){
            int id = Integer.parseInt(idOrPhone);
            if(repository.existsById(id)){
                return repository.findById(id).get();
            }
            return null;
        }
        if(repository.existsByPhone(idOrPhone)){
            return repository.findByPhone(idOrPhone).get();
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
