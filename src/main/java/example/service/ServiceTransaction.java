package example.service;

import example.entity.Account;
import example.entity.Bank;
import example.entity.Transaction;
import example.entity.User;
import example.exception.AmountException;
import example.exception.BalanceException;
import example.exception.ScaleException;
import example.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class ServiceTransaction {
    @Autowired
    private TransactionRepository repository;

    @Transactional
    public Transaction add(Account send, Account get, BigDecimal amount){
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AmountException();
        }
        if(amount.scale() > 2){
            throw new ScaleException();
        }
        BigDecimal newBalance = send.getBalance().subtract(amount);
        if(newBalance.compareTo(BigDecimal.ZERO) < 0){
            throw new BalanceException();
        }

        send.setBalance(newBalance);
        get.setBalance(get.getBalance().add(amount));

        Transaction transaction = new Transaction();
        transaction.setAccountGet(get);
        transaction.setAccountSend(send);
        transaction.setAmount(amount);
        transaction.setDate(new Date());

        return repository.save(transaction);
    }

    public Iterable<Transaction> search(Account accountSend, Account accountGet, String sortMode){
        if(sortMode.equals("Asc")){
            return repository.findByAccountSendAndAccountGetOrderByDateAsc(accountSend, accountGet);
        }
        if(sortMode.equals("Desc")){
            return repository.findByAccountSendAndAccountGetOrderByDateDesc(accountSend, accountGet);
        }
        return null;
    }

    public Iterable<Transaction> getTransaction(User user){
        return repository.findByUser(user.getId());
    }

    public Iterable<Transaction> getTransactionByBank(User user, Bank bank){
        return repository.findByUserAndBank(user.getId(), bank.getId());
    }
}
