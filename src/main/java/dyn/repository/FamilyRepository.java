package dyn.repository;

import dyn.model.Family;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyRepository extends CrudRepository<Family, Long> {
    public Family findByFamilyName(String familyName);
}