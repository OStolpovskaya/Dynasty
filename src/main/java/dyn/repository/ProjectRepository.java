package dyn.repository;

import dyn.model.Family;
import dyn.model.Project;
import dyn.model.ProjectStatus;
import dyn.model.Thing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
    Project findByName(String thingName);

    List<Project> findByThing(Thing thing);

    List<Project> findByThingAndStatus(Thing thing, ProjectStatus status);

    List<Project> findByThingAndStatusOrderByCostAsc(Thing thing, ProjectStatus status);

    List<Project> findByAuthor(Family author);

    List<Project> findAllByStatusOrderByThing(ProjectStatus projectStatus);

    List<Project> findAllByThingCraftBranchIdOrderByCostAscThingIdAsc(Long craftBranchId);

    List<Project> findByThingAndAuthorId(Thing thing, long authorId);
}