package dyn.repository;

import dyn.model.Character;
import dyn.model.Family;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends CrudRepository<Character, Long> {
    public List<Character> findByFamilyAndLevel(Family family, int level);

    public List<Character> findByFamilyAndLevelOrderByFatherAsc(Family family, int level);

    @Query(value = "SELECT value FROM names_male ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public String getRandomNameMale();

    @Query(value = "SELECT value FROM names_female ORDER BY RAND() LIMIT 1", nativeQuery = true)
    public String getRandomNameFemale();

    List<Character> findByFamilyAndLevelAndSexAndSpouseIsNotNull(Family family, int level, String sex);

    List<Character> findByFamilyAndLevelAndSexAndSpouseIsNull(Family family, int level, String sex);
}