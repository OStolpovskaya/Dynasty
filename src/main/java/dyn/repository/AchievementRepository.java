package dyn.repository;

import dyn.model.Achievement;
import dyn.model.AchievementType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Long> {
    Achievement findByName(String name);

    Achievement findByTypeAndForWhat(AchievementType type, long forWhat);

    List<Achievement> findAllByOrderByTypeAscForWhatAsc();
}