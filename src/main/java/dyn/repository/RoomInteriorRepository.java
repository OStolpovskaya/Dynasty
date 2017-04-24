package dyn.repository;

import dyn.model.RoomInterior;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomInteriorRepository extends CrudRepository<RoomInterior, Long> {
    List<RoomInterior> findByHouseIdLessThanEqualAndRoomIdOrderById(long houseId, long roomId);
}