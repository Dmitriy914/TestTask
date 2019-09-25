package example.controller;

import example.entity.Bank;
import example.model.BankModel;
import example.service.ServiceBank;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/banks")
public class ControllerBank {
    private final ServiceBank service;

    public ControllerBank(ServiceBank service) {
        this.service = service;
    }

    @PostMapping
    public Bank add(@Valid @RequestBody BankModel model){
        return service.add(model);
    }

    @GetMapping
    public Iterable<Bank> search() {
        return service.searchAll();
    }
}
