package dyn.repository;

import dyn.model.Family;
import dyn.model.User;
import dyn.model.UserType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyRepository extends CrudRepository<Family, Long> {
    public Family findByFamilyName(String familyName);

    Family findByUserAndCurrentIsTrue(User user);

    Family findByIdAndUser(Long familyId, User player);

    List<Family> findAllByOrderByUserLastLoginDateDesc();

    List<Family> findTop10ByUserTypeOrderByMoneyDesc(UserType userType);

    List<Family> findTop10ByUserTypeOrderByLevelDesc(UserType userType);


}