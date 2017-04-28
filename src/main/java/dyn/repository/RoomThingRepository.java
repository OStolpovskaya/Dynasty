package dyn.repository;

import dyn.model.RoomThing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomThingRepository extends CrudRepository<RoomThing, Long> {
    List<RoomThing> findByHouseIdLessThanEqualAndRoomIdOrderById(long houseId, long roomId);

    List<RoomThing> findByHouseIdAndRoomIdOrderById(Long houseId, Long roomId);
}