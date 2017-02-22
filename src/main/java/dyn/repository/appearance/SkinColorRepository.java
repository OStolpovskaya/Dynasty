package dyn.repository.appearance;

import dyn.model.appearance.SkinColor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkinColorRepository extends CrudRepository<SkinColor, Long> {
    public SkinColor findByName(String name);
}