package dyn.repository;

import dyn.model.Character;
import dyn.model.Family;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends CrudRepository<Character, Long> {
    public Character findByFamilyAndLevel(Family family, int level);
}