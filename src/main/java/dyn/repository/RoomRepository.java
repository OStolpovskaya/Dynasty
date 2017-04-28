package dyn.repository;

import dyn.model.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
    List<Room> findByHouseIdLessThanEqualOrderById(long houseId);

    List<Room> findByHouseIdOrderById(Long id);
}