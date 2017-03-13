package dyn.repository;

import dyn.model.Race;
import dyn.model.RaceAppearance;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RaceAppearanceRepository extends CrudRepository<RaceAppearance, Long> {
    public RaceAppearance findByRace(Race race);

    @Modifying
    @Transactional
    void deleteByRace(Race race);


}