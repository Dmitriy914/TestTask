package example.controller;

import example.entity.Account;
import example.entity.Transaction;
import example.model.TransactionModel;
import example.service.ServiceAccount;
import example.service.ServiceTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/transactions")
public class ControllerTransaction {
    @Autowired
    private ServiceTransaction serviceTransaction;

    @Autowired
    private ServiceAccount serviceAccount;

    @PostMapping
    public Transaction Add(@RequestBody TransactionModel model){
        Account send = serviceAccount.search(model.getAccountSendIdOrAccountNumber());
        Account get = serviceAccount.search(model.getAccountGetIdOrAccountNumber());

        if(get == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account get not found");
        if(send == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account send not found");

        return serviceTransaction.add(send, get, model.getAmount());
    }

    @GetMapping
    public Iterable<Transaction> Search(@RequestParam String accountSend,
                                        @RequestParam String accountGet,
                                        @RequestParam String sortMode){
        Account get = serviceAccount.search(accountGet);
        Account send = serviceAccount.search(accountSend);

        if(get == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account get not found");
        if(send == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account send not found");

        return serviceTransaction.search(send, get, sortMode);
    }
}
