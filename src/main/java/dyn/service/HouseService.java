package dyn.service;

import dyn.model.*;
import dyn.repository.ItemRepository;
import dyn.repository.RoomInteriorRepository;
import dyn.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OM on 30.03.2017.
 */
@Service
public class HouseService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomInteriorRepository roomInteriorRepository;

    @Autowired
    private ItemRepository itemRepository;


    public HouseInterior getHouseInterior(Family family) {

        House house = family.getHouse();

        HouseInterior houseInterior = new HouseInterior();
        houseInterior.setFamily(family);
        houseInterior.setHouse(house);

        List<Room> rooms = roomRepository.findByHouseIdLessThanEqualOrderById(house.getId());
        for (Room room : rooms) {
            List<RoomInterior> interiorList = roomInteriorRepository.findByHouseAndRoom(house, room);
            for (RoomInterior roomInterior : interiorList) {
                RoomInteriorWithItems roomInteriorWithItems = new RoomInteriorWithItems(roomInterior);

                Item currentItem = null;
                List<Item> availableItems = new ArrayList<>();

                List<Item> itemList = itemRepository.findByFamilyAndProjectThing(family, roomInterior.getThing());
                for (Item availableItem : itemList) {
                    if (availableItem.getInteriorId() == roomInterior.getId()) {
                        currentItem = availableItem;
                    }
                    if (availableItem.getInteriorId() == 0) {
                        availableItems.add(availableItem);
                    }
                }
                roomInteriorWithItems.setCurrentItem(currentItem);
                roomInteriorWithItems.setAvailableItems(availableItems);
                houseInterior.addRoomInterior(room, roomInteriorWithItems);
            }

        }

        return houseInterior;
    }
}
