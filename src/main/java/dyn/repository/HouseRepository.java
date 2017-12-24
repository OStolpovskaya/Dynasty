package dyn.repository;

import dyn.model.House;
import dyn.model.HouseType;
import dyn.model.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseRepository extends CrudRepository<House, Long> {
    List<House> findByName(String name);

    List<House> findAllByType(HouseType type);

    List<House> findAllByTypeOrderByPairsNum(HouseType type);

    House findByProduction(Project project);

    @Query(value = "SELECT house.name,COUNT(*) " +
            "FROM family JOIN house ON family.house_id=house.id JOIN users ON family.`user_id`=users.userid WHERE users.type='player' " +
            "GROUP BY house_id ORDER BY family.house_id", nativeQuery = true)
    List<Object[]> countHouses();

    @Query(value = "SELECT house.name,COUNT(*) " +
            "FROM family_building JOIN house ON family_building.building_id=house.id JOIN family ON family_building.family_id=family.id JOIN users ON family.`user_id`=users.userid WHERE users.type='player' " +
            "GROUP BY building_id ORDER BY `house`.`pairs_num`", nativeQuery = true)
    List<Object[]> countBuildings();
}