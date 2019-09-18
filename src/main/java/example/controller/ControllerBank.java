package example.controller;

import example.entity.Bank;
import example.model.BankModel;
import example.service.ServiceBank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/banks")
public class ControllerBank {
    @Autowired
    private ServiceBank service;

    @PostMapping
    public Bank Add(@RequestBody BankModel model){
        Bank bank = new Bank();
        bank.setName(model.getName());
        bank.setAddress(model.getAddress());
        bank.setPhone(model.getPhone());
        return service.add(bank);
    }

    @GetMapping
    public Iterable<Bank> Search() {
        return service.searchAll();
    }
}
