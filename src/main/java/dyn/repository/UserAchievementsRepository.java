package dyn.repository;

import dyn.model.User;
import dyn.model.UserAchievements;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAchievementsRepository extends CrudRepository<UserAchievements, Long> {
    List<UserAchievements> findByUser(User user);

    @Query(value = "SELECT user_userid, COUNT( * ) AS count FROM user_achievements GROUP BY user_userid ORDER BY count DESC LIMIT 10", nativeQuery = true)
    List<Object[]> countAchievements();
}