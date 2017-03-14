package dyn.repository;

import dyn.model.Achievement;
import dyn.model.AchievementType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Long> {
    Achievement findByName(String name);

    Achievement findByTypeAndForWhat(AchievementType type, String forWhat);
}