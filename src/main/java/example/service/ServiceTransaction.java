package example.service;

import example.entity.Account;
import example.entity.Transaction;
import example.exception.BalanceException;
import example.exception.NotFoundException;
import example.exception.ScaleException;
import example.model.SortMode;
import example.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Instant;

@Service
public class ServiceTransaction {
    private final TransactionRepository repository;
    private final ServiceAccount serviceAccount;
    private final ServiceUser serviceUser;
    private final ServiceBank serviceBank;

    public ServiceTransaction(TransactionRepository repository, ServiceAccount serviceAccount, ServiceUser serviceUser, ServiceBank serviceBank) {
        this.repository = repository;
        this.serviceAccount = serviceAccount;
        this.serviceUser = serviceUser;
        this.serviceBank = serviceBank;
    }

    @Transactional
    public Transaction add(String accountSend, String accountGet, BigDecimal amount){
        Account send = serviceAccount.search(accountSend);
        Account get = serviceAccount.search(accountGet);

        if(send == null) throw new NotFoundException("Account send");
        if(get == null) throw new NotFoundException("Account get");

        send.setBalance(send.getBalance().subtract(amount));
        get.setBalance(get.getBalance().add(amount));

        if(amount.scale() > 2) throw new ScaleException();
        if(send.getBalance().compareTo(BigDecimal.ZERO) < 0) throw new BalanceException();

        Transaction transaction = new Transaction();
        transaction.setAccountGet(get);
        transaction.setAccountSend(send);
        transaction.setAmount(amount);
        transaction.setInstant(Instant.now());

        return repository.save(transaction);
    }

    @Transactional
    public Iterable<Transaction> search(String accountSend, String accountGet, SortMode sortMode){
        Account send = serviceAccount.search(accountSend);
        Account get = serviceAccount.search(accountGet);

        if(send == null) throw new NotFoundException("Account send");
        if(get == null) throw new NotFoundException("Account get");

        switch(sortMode){
            case ASC: return repository.findByAccountSendAndAccountGetOrderByInstantAsc(send, get);
            case DESC: return repository.findByAccountSendAndAccountGetOrderByInstantDesc(send, get);
            default: return null;
        }
    }

    @Transactional
    public Iterable<Transaction> getTransaction(String userIdOrPhone){
        return repository.findByUser(serviceUser.search(userIdOrPhone).getId());
    }

    @Transactional
    public Iterable<Transaction> getTransactionByBank(String user, String bank){
        return repository.findByUserAndBank(serviceUser.search(user).getId(), serviceBank.search(bank).getId());
    }
}
