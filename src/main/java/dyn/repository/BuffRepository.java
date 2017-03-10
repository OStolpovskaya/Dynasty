package dyn.repository;

import dyn.model.Buff;
import dyn.model.BuffType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuffRepository extends CrudRepository<Buff, Long> {
    Buff findByTitle(String title);

    List<Buff> findByType(BuffType buffType);
}