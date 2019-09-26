package example.controller;

import example.entity.Account;
import example.entity.Transaction;
import example.model.TransactionModel;
import example.service.ServiceAccount;
import example.service.ServiceTransaction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/transactions")
public class ControllerTransaction {
    private final ServiceTransaction serviceTransaction;

    private final ServiceAccount serviceAccount;

    public ControllerTransaction(ServiceTransaction serviceTransaction, ServiceAccount serviceAccount) {
        this.serviceTransaction = serviceTransaction;
        this.serviceAccount = serviceAccount;
    }

    @PostMapping
    public Transaction add(@Valid @RequestBody TransactionModel model){
        Account send = serviceAccount.search(model.getAccountSendIdOrAccountNumber());
        Account get = serviceAccount.search(model.getAccountGetIdOrAccountNumber());

        if(send == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account send not found");
        if(get == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account get not found");

        return serviceTransaction.add(send, get, model.getAmount());
    }

    @GetMapping
    public Iterable<Transaction> search(@RequestParam String accountSendIdOrAccountNumber,
                                        @RequestParam String accountGetIdOrAccountNumber,
                                        @RequestParam String sortMode){
        validate(accountSendIdOrAccountNumber, accountGetIdOrAccountNumber, sortMode);

        Account get = serviceAccount.search(accountGetIdOrAccountNumber);
        Account send = serviceAccount.search(accountSendIdOrAccountNumber);

        if(send == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account send not found");
        if(get == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account get not found");

        return serviceTransaction.search(send, get, sortMode);
    }

    private void validate(String send, String get, String sortMode){
        if(send.equals(""))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameter (accountSendIdOrAccountNumber) should not be empty");
        if(get.equals(""))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameter (accountGetIdOrAccountNumber) should not be empty");
        if(!sortMode.equals("Asc") && !sortMode.equals("Desc"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameter (sortMode) should be (Asc) or (Desc)");
    }
}
