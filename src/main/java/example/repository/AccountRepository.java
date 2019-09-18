package example.repository;

import example.entity.Bank;
import example.entity.User;
import org.springframework.data.repository.CrudRepository;
import example.entity.Account;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Integer> {
    boolean existsByAccountNumber(String accountNumber);
    boolean existsByUserAndBank(User user, Bank bank);
    Optional<Account> findByAccountNumber(String accountNumber);
    Iterable<Account> findByUserId(Integer id);
}
