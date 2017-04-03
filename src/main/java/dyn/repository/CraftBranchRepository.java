package dyn.repository;

import dyn.model.CraftBranch;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CraftBranchRepository extends CrudRepository<CraftBranch, Long> {
    CraftBranch findByName(String name);
}