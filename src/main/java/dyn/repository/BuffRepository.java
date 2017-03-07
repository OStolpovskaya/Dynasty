package dyn.repository;

import dyn.model.Buff;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuffRepository extends CrudRepository<Buff, Long> {
    Buff findByTitle(String title);
}