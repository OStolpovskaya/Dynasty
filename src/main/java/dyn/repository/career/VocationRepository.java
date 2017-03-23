package dyn.repository.career;

import dyn.model.career.Vocation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocationRepository extends CrudRepository<Vocation, Long> {
    @Query(value = "SELECT * FROM vocation ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Vocation getRandom();
}
