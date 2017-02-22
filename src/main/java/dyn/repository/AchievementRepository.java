package dyn.repository;

import dyn.model.Achievement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Long> {
    public Achievement findByName(String name);
}