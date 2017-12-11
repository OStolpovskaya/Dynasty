package dyn.repository;

import dyn.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUserName(String username);

    User findByEmail(String email);

    User findByResetToken(String resetToken);

    List<User> findAllByOrderByLastLoginDateDesc();
}