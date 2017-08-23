package dyn.repository;

import dyn.model.User;
import dyn.model.UserAchievements;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAchievementsRepository extends CrudRepository<UserAchievements, Long> {
    List<UserAchievements> findByUser(User user);

}