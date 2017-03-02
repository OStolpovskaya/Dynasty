package dyn.repository;

import dyn.model.Character;
import dyn.model.Family;
import dyn.model.Fiancee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FianceeRepository extends CrudRepository<Fiancee, Long> {
    public Fiancee findByCharacter(Character character);

    public List<Fiancee> findByCharacterFamilyNotAndCharacterLevel(Family family, int level);

}