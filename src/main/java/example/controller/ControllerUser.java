package example.controller;

import example.entity.*;
import example.model.UserModel;
import example.service.ServiceAccount;
import example.service.ServiceBank;
import example.service.ServiceTransaction;
import example.service.ServiceUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class ControllerUser {
    private final ServiceUser serviceUser;

    private final ServiceBank serviceBank;

    private final ServiceAccount serviceAccount;

    private final ServiceTransaction serviceTransaction;

    public ControllerUser(ServiceUser serviceUser, ServiceBank serviceBank, ServiceAccount serviceAccount, ServiceTransaction serviceTransaction) {
        this.serviceUser = serviceUser;
        this.serviceBank = serviceBank;
        this.serviceAccount = serviceAccount;
        this.serviceTransaction = serviceTransaction;
    }

    @PostMapping()
    public User Add(@Valid @RequestBody UserModel model){
        return serviceUser.add(model);
    }

    @GetMapping("/{idOrPhone}")
    public User Search(@PathVariable("idOrPhone") String idOrPhone){
        User user = serviceUser.search(idOrPhone);

        if(user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        return user;
    }

    @GetMapping("/{idOrPhone}/accounts")
    public Iterable<Account> GetAccounts(@PathVariable("idOrPhone") String idOrPhone){
        User user = serviceUser.search(idOrPhone);

        if(user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        return serviceAccount.getAccountsByUser(user);
    }

    @GetMapping("/{idOrPhone}/transactions")
    public Iterable<Transaction> GetTransaction(@PathVariable("idOrPhone") String userIdOrPhone,
                                                @RequestParam(defaultValue = "none") String bankIdOrNameOrPhone){
        User user = serviceUser.search(userIdOrPhone);

        if(user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        if(bankIdOrNameOrPhone.equals("none")) return serviceTransaction.getTransaction(user);

        Bank bank = serviceBank.search(bankIdOrNameOrPhone);

        if(bank == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank not found");

        return serviceTransaction.getTransactionByBank(user, bank);
    }
}
