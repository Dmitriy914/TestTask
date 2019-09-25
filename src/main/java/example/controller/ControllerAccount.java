package example.controller;

import example.entity.Account;
import example.entity.Bank;
import example.entity.User;
import example.model.AccountModel;
import example.service.ServiceAccount;
import example.service.ServiceBank;
import example.service.ServiceUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class ControllerAccount {
    private final ServiceAccount serviceAccount;

    private final ServiceUser serviceUser;

    private final ServiceBank serviceBank;

    public ControllerAccount(ServiceAccount serviceAccount, ServiceUser serviceUser, ServiceBank serviceBank) {
        this.serviceAccount = serviceAccount;
        this.serviceUser = serviceUser;
        this.serviceBank = serviceBank;
    }

    @PostMapping
    public Account add(@Valid @RequestBody AccountModel model){
        User user = serviceUser.search(model.getUserIdOrPhone());
        Bank bank = serviceBank.search(model.getBankIdOrNameOrPhone());

        if(user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        if(bank == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank not found");

        return serviceAccount.add(user, bank);
    }
}
