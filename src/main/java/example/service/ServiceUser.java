package example.service;

import example.entity.User;
import example.exception.DuplicateException;
import example.model.UserModel;
import example.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ServiceUser {
    private final UserRepository repository;

    public ServiceUser(UserRepository repository) {
        this.repository = repository;
    }

    public User add(UserModel model){
        if(repository.existsByPhone(model.getPhone())){
            throw new DuplicateException("phone");
        }
        User user = new User();
        user.setAddress(model.getAddress());
        user.setName(model.getName());
        user.setSurname(model.getSurname());
        user.setPatronymic(model.getPatronymic());
        user.setPhone(model.getPhone());
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
