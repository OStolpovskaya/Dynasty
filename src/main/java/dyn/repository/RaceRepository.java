package dyn.repository;

import dyn.model.Race;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaceRepository extends CrudRepository<Race, Long> {
    List<Race> findAllByOrderByName();
}