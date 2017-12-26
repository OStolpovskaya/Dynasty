package dyn.repository;

import dyn.model.User;
import dyn.model.UserNeighbor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNeighborRepository extends CrudRepository<UserNeighbor, Long> {
    List<UserNeighbor> findAllByUserOrderByDateDesc(User user);

}