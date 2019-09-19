package example.repository;

import org.springframework.data.repository.CrudRepository;
import example.entity.Bank;

import java.util.Optional;

public interface BankRepository extends CrudRepository<Bank, Integer> {
    boolean existsByName(String name);
    boolean existsByPhone(String phone);
    Optional<Bank> findByName(String name);
    Optional<Bank> findByPhone(String phone);
}
