package example.controller;

import example.entity.Transaction;
import example.exception.EmptyException;
import example.model.SortMode;
import example.model.TransactionModel;
import example.service.ServiceTransaction;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/transactions")
public class ControllerTransaction {
    private final ServiceTransaction serviceTransaction;

    public ControllerTransaction(ServiceTransaction serviceTransaction) {
        this.serviceTransaction = serviceTransaction;
    }

    @PostMapping
    public Transaction add(@Valid @RequestBody TransactionModel model){
        return serviceTransaction.add(model.getAccountSendIdOrAccountNumber(),
                model.getAccountGetIdOrAccountNumber(), model.getAmount());
    }

    @GetMapping
    public Iterable<Transaction> search(@RequestParam String accountSendIdOrAccountNumber,
                                        @RequestParam String accountGetIdOrAccountNumber,
                                        @RequestParam SortMode sortMode){
        if(accountSendIdOrAccountNumber.equals("")) throw new EmptyException("accountSendIdOrAccountNumber");
        if(accountGetIdOrAccountNumber.equals("")) throw new EmptyException("accountGetIdOrAccountNumber");
        if(sortMode == null) throw new EmptyException("sortMode");

        return serviceTransaction.search(accountSendIdOrAccountNumber, accountGetIdOrAccountNumber, sortMode);
    }
}
