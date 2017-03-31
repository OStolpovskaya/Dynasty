package dyn.repository;

import dyn.model.Project;
import dyn.model.Thing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
    Project findByName(String thingName);

    List<Project> findByThing(Thing thing);
}