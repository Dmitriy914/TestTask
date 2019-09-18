package example.controller;

import example.entity.Account;
import example.entity.Bank;
import example.entity.User;
import example.model.AccountModel;
import example.service.ServiceAccount;
import example.service.ServiceBank;
import example.service.ServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/accounts")
public class ControllerAccount {
    @Autowired
    private ServiceAccount serviceAccount;

    @Autowired
    private ServiceUser serviceUser;

    @Autowired
    private ServiceBank serviceBank;

    @PostMapping
    public Account Add(@RequestBody AccountModel model){
        Account account = new Account();
        User user = serviceUser.search(model.getUserIdOrPhone());
        Bank bank = serviceBank.search(model.getBankIdOrNameOrPhone());

        if(user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        if(bank == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank not found");

        account.setUser(user);
        account.setBank(bank);
        return serviceAccount.add(account);
    }
}
