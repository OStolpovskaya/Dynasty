package dyn.repository;

import dyn.model.Character;
import dyn.model.Family;
import dyn.model.Fiancee;
import dyn.model.Race;
import dyn.model.appearance.AppearanceType;
import dyn.model.career.Vocation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FianceeRepository extends CrudRepository<Fiancee, Long> {
    Fiancee findByCharacter(Character character);

    List<Fiancee> findByCharacterFamilyNotAndCharacterLevel(Family family, int level);

    List<Fiancee> findByCharacterRaceAndCharacterCareerVocationAndCharacterEarsType(Race race, Vocation vocation, AppearanceType type);

    List<Fiancee> findByCharacterRace(Race race);

    List<Fiancee> findByCharacterFamilyNotAndCharacterLevelAndCharacterRace(Family family, int level, Race race);
}