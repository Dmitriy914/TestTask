package example.repository;

import org.springframework.data.repository.CrudRepository;
import example.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository()
public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByPhone(String phone);
    Optional<User> findByPhone(String phone);
}
