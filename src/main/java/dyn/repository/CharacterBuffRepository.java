package dyn.repository;

import dyn.model.Buff;
import dyn.model.CharacterBuff;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterBuffRepository extends CrudRepository<CharacterBuff, Long> {
    Buff findByCharacter(Character character);
}