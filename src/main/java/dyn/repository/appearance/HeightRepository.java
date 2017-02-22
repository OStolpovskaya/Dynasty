package dyn.repository.appearance;

import dyn.model.appearance.Height;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeightRepository extends CrudRepository<Height, Long> {
    public Height findByName(String name);
}