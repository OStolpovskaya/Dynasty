package dyn.repository;

import dyn.model.Family;
import dyn.model.FamilyProject;
import dyn.model.Project;
import dyn.model.Thing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyProjectRepository extends CrudRepository<FamilyProject, Long> {
    List<FamilyProject> findByFamily(Family family);

    List<FamilyProject> findByFamilyAndProjectThing(Family family, Thing thing);

    FamilyProject findByFamilyAndProject(Family family, Project project);

}