package example.controller;

import example.entity.*;
import example.model.UserModel;
import example.service.ServiceAccount;
import example.service.ServiceBank;
import example.service.ServiceTransaction;
import example.service.ServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class ControllerUser {
    @Autowired
    private ServiceUser serviceUser;

    @Autowired
    private ServiceBank serviceBank;

    @Autowired
    private ServiceAccount serviceAccount;

    @Autowired
    private ServiceTransaction serviceTransaction;

    @PostMapping()
    public User Add(@RequestBody UserModel model){
        User NewUser = new User();
        NewUser.setAddress(model.getAddress());
        NewUser.setName(model.getName());
        NewUser.setSurname(model.getSurname());
        NewUser.setPatronymic(model.getPatronymic());
        NewUser.setPhone(model.getPhone());
        return serviceUser.Add(NewUser);
    }

    @GetMapping("/{idOrPhone}")
    public User Search(@PathVariable("idOrPhone") String idOrPhone){
        User user = serviceUser.Search(idOrPhone);

        if(user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        return user;
    }

    @GetMapping("/{idOrPhone}/accounts")
    public Iterable<Account> GetAccounts(@PathVariable("idOrPhone") String idOrPhone){
        User user = serviceUser.Search(idOrPhone);

        if(user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        return serviceAccount.GetAccountsByUser(user);
    }

    @GetMapping("/{idOrPhone}/transactions")
    public Iterable<Transaction> GetTransaction(@PathVariable("idOrPhone") String user_idOrPhone,
                                                @RequestParam(name = "bank", defaultValue = "none") String bank_idOrNameOrPhone){
        User user = serviceUser.Search(user_idOrPhone);

        if(user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        if(bank_idOrNameOrPhone.equals("none")){
            return serviceTransaction.GetTransaction(user);
        }

        Bank bank = serviceBank.Search(bank_idOrNameOrPhone);

        if(bank == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank not found");

        return serviceTransaction.GetTransactionByBank(user, bank);
    }
}
