package example.controller;

import example.entity.Account;
import example.model.AccountModel;
import example.service.ServiceAccount;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class ControllerAccount {
    private final ServiceAccount serviceAccount;

    public ControllerAccount(ServiceAccount serviceAccount) {
        this.serviceAccount = serviceAccount;
    }

    @PostMapping
    public Account add(@Valid @RequestBody AccountModel model){
        return serviceAccount.add(model.getUserIdOrPhone(), model.getBankIdOrNameOrPhone());
    }
}
