package dyn.repository;

import dyn.model.Family;
import dyn.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyRepository extends CrudRepository<Family, Long> {
    public Family findByFamilyName(String familyName);


    Family findByIdAndUser(Long familyId, User player);
}