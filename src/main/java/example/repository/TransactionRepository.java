package example.repository;

import example.entity.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import example.entity.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    Iterable<Transaction> findByAccountSendAndAccountGetOrderByDateAsc(Account send, Account get);
    Iterable<Transaction> findByAccountSendAndAccountGetOrderByDateDesc(Account send, Account get);

    @Query(value = "SELECT * FROM TRANSACTION INNER JOIN account ON((transaction.account_send_id = account.id) OR (transaction.account_get_id = account.id)) where user_id = ?1", nativeQuery = true)
    Iterable<Transaction> findByUser(Integer id);

    @Query(value = "SELECT * FROM TRANSACTION INNER JOIN account ON((transaction.account_send_id = account.id) OR (transaction.account_get_id = account.id)) where (user_id = ?1) and (bank_id = ?2)", nativeQuery = true)
    Iterable<Transaction> findByUserAndBank(Integer userId, Integer bankId);
}
