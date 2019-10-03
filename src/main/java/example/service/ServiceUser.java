package example.service;

import example.entity.User;
import example.exception.DuplicateException;
import example.exception.NotFoundException;
import example.model.UserModel;
import example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceUser {
    private final UserRepository repository;

    public ServiceUser(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
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
        User user;
        if(checkNumeric(idOrPhone)) user = repository.findById(Integer.parseInt(idOrPhone)).orElse(null);
        else user = repository.findByPhone(idOrPhone).orElse(null);

        if(user == null) throw new NotFoundException("User");
        else return user;
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
