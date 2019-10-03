package example.controller;

import example.entity.*;
import example.model.UserModel;
import example.service.ServiceAccount;
import example.service.ServiceBank;
import example.service.ServiceTransaction;
import example.service.ServiceUser;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class ControllerUser {
    private final ServiceUser serviceUser;
    private final ServiceAccount serviceAccount;
    private final ServiceTransaction serviceTransaction;

    public ControllerUser(ServiceUser serviceUser, ServiceAccount serviceAccount, ServiceTransaction serviceTransaction) {
        this.serviceUser = serviceUser;
        this.serviceAccount = serviceAccount;
        this.serviceTransaction = serviceTransaction;
    }

    @PostMapping()
    public User add(@Valid @RequestBody UserModel model){
        return serviceUser.add(model);
    }

    @GetMapping("/{idOrPhone}")
    public User search(@PathVariable("idOrPhone") String idOrPhone){
        return serviceUser.search(idOrPhone);
    }

    @GetMapping("/{idOrPhone}/accounts")
    public Iterable<Account> getAccounts(@PathVariable("idOrPhone") String idOrPhone){
        return serviceAccount.getAccountsByUser(idOrPhone);
    }

    @GetMapping("/{idOrPhone}/transactions")
    public Iterable<Transaction> getTransaction(@PathVariable("idOrPhone") String userIdOrPhone,
                                                @RequestParam(defaultValue = "none") String bankIdOrNameOrPhone){
        if(bankIdOrNameOrPhone.equals("none")) return serviceTransaction.getTransaction(userIdOrPhone);
        else return serviceTransaction.getTransactionByBank(userIdOrPhone, bankIdOrNameOrPhone);
    }
}
