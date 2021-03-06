package dyn.repository;

import dyn.model.Achievement;
import dyn.model.User;
import dyn.model.UserAchievements;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAchievementsRepository extends CrudRepository<UserAchievements, Long> {
    List<UserAchievements> findByUser(User user);

    UserAchievements findByUserAndAchievement(User user, Achievement achievement);

    List<UserAchievements> findByUserOrderByDate(User user);

    @Query(value = "SELECT user_userid, COUNT( * ) AS count FROM user_achievements JOIN users ON users.userid = user_achievements.user_userid WHERE users.type = 'player' GROUP BY user_userid ORDER BY count DESC LIMIT 10", nativeQuery = true)
    List<Object[]> countAchievements();
}