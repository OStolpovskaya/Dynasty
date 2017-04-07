package dyn.repository;

import dyn.model.Family;
import dyn.model.FamilyLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyLogRepository extends CrudRepository<FamilyLog, Long> {
    List<FamilyLog> findByFamily(Family family);

    FamilyLog findByFamilyAndLevel(Family family, int level);
}