package dyn.repository.career;

import dyn.model.career.Education;
import dyn.model.career.Profession;
import dyn.model.career.Vocation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessionRepository extends CrudRepository<Profession, Long> {
    public List<Profession> findByVocationAndEducationAndIntelligenceLessThanEqualAndCharismaLessThanEqualAndStrengthLessThanEqualAndCreativityLessThanEqual(Vocation vocation, Education education, int intelligence, int charisma, int strength, int creativity);


    Profession findFirstByVocationAndIntelligenceLessThanEqualAndCharismaLessThanEqualAndStrengthLessThanEqualAndCreativityLessThanEqualOrderByLevelDesc(Vocation vocation, int intelligence, int charisma, int strength, int creativity);

    List<Profession> findByVocationAndIntelligenceLessThanEqualAndCharismaLessThanEqualAndStrengthLessThanEqualAndCreativityLessThanEqual(Vocation vocation, int intelligence, int charisma, int strength, int creativity);

    Profession findByVocationAndLevel(Vocation vocation, int level);
}
