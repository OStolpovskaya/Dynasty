package dyn.repository;

import dyn.model.House;
import dyn.model.RoomThing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomThingRepository extends CrudRepository<RoomThing, Long> {
    List<RoomThing> findByHouseIdLessThanEqualAndRoomIdOrderByLayerAsc(long houseId, long roomId);

    List<RoomThing> findByHouseIdAndRoomIdOrderByLayerAsc(Long houseId, Long roomId);

    List<RoomThing> findAllByHouseOrderByRoomAscThingAsc(House house);
}