package dyn.repository.appearance;

import dyn.model.appearance.Head;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeadRepository extends CrudRepository<Head, Long> {
    public Head findByName(String name);

    @Query(value = "SELECT * FROM app_head ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public Head getRandom();
}