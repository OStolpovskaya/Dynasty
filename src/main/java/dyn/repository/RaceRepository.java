package dyn.repository;

import dyn.model.Race;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaceRepository extends CrudRepository<Race, Long> {
    public Race findByName(String name);
}