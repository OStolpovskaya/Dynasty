package dyn.repository;

import dyn.model.House;
import dyn.model.Room;
import dyn.model.RoomInterior;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomInteriorRepository extends CrudRepository<RoomInterior, Long> {
    List<RoomInterior> findByHouseAndRoom(House house, Room room);

    List<RoomInterior> findByHouseIdLessThanEqualAndRoomOrderById(long houseId, Room room);
}