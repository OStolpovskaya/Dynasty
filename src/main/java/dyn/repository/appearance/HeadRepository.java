package dyn.repository.appearance;

import dyn.model.appearance.Head;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeadRepository extends CrudRepository<Head, Long> {
    public Head findByName(String name);
}