package dyn.service;

import dyn.model.*;
import dyn.repository.ItemRepository;
import dyn.repository.RoomInteriorRepository;
import dyn.repository.RoomRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OM on 30.03.2017.
 */
@Service
public class HouseService {

    private static final Logger logger = LogManager.getLogger(HouseService.class);
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

    public boolean setItemToRoomInterior(Family family, Long itemId, Long roomInteriorId) {
        Item item = itemRepository.findByFamilyAndId(family, itemId);
        if (item == null) {
            logger.error("Item not found: family.id=" + family.getId() + ", item.id=" + itemId);
            return false;
        }
        RoomInterior roomInterior = roomInteriorRepository.findOne(roomInteriorId);
        if (roomInterior == null) {
            logger.error("RoomInterior not found: roomInterior.id=" + roomInteriorId);
            return false;
        }

        Item oldItem = itemRepository.findByFamilyAndInteriorId(family, roomInterior.getId());
        if (oldItem == null) {
            logger.debug("Family " + family.getFamilyName() + " has no item with interiorId=" + roomInterior.getId());
        } else {
            oldItem.setInteriorId(0L);
            logger.debug("Family " + family.getFamilyName() + " has item with interiorId=" + roomInterior.getId() + ". Set it to 0");
            itemRepository.save(oldItem);
        }

        item.setInteriorId(roomInterior.getId());
        logger.debug("Family " + family.getFamilyName() + " set item " + item.getProject().getName() + "(" + item.getId() + ") to room interior thing=" + roomInterior.getThing().getName());
        itemRepository.save(item);
        return true;
    }

    public boolean unsetItem(Family family, Long itemId) {
        Item item = itemRepository.findByFamilyAndId(family, itemId);
        if (item == null) {
            logger.error("Item not found: family.id=" + family.getId() + ", item.id=" + itemId);
            return false;
        }
        if (item.getInteriorId() == 0) {
            logger.error("item.getInteriorId() == 0 item.id = " + item.getId());
            return false;
        }

        RoomInterior roomInterior = roomInteriorRepository.findOne(item.getInteriorId());
        if (roomInterior == null) {
            logger.error("RoomInterior not found: roomInterior.id=" + item.getInteriorId());
            return false;
        }

        item.setInteriorId(0L);
        logger.debug("Family " + family.getFamilyName() + " unset item " + item.getProject().getName() + "(" + item.getId() + ") from room interior thing=" + roomInterior.getThing().getName());
        itemRepository.save(item);
        return true;
    }

    public List<Item> getItemsInStorage(Family family) {
        List<Item> itemsInStorage = new ArrayList<>();
        List<Item> familyItems = itemRepository.findByFamily(family);
        for (Item familyItem : familyItems) {
            if (familyItem.getInteriorId() == 0) {
                itemsInStorage.add(familyItem);
            }
        }
        return itemsInStorage;
    }
}
