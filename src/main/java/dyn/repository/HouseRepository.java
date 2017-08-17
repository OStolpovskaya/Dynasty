package dyn.repository;

import dyn.model.House;
import dyn.model.HouseType;
import dyn.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseRepository extends CrudRepository<House, Long> {
    List<House> findByName(String name);

    List<House> findAllByType(HouseType type);

    List<House> findAllByTypeOrderByPairsNum(HouseType type);

    House findByProduction(Project project);
}