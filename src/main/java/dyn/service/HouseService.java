package dyn.service;

import dyn.model.*;
import dyn.repository.*;
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
    private HouseRepository houseRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ThingRepository thingRepository;

    public HouseInterior getHouseInterior(Family family) {

        House house = family.getHouse();

        HouseInterior houseInterior = new HouseInterior();
        houseInterior.setFamily(family);
        houseInterior.setHouse(house);

        boolean allItems = true;
        List<Room> rooms = roomRepository.findByHouseIdLessThanEqualOrderById(house.getId());
        for (Room room : rooms) {
            List<RoomInterior> interiorList = roomInteriorRepository.findByHouseIdLessThanEqualAndRoomOrderById(house.getId(), room);
            for (RoomInterior roomInterior : interiorList) {
                RoomInteriorWithItems roomInteriorWithItems = new RoomInteriorWithItems(roomInterior);

                Item currentItem = null;
                List<Item> availableItems = new ArrayList<>();

                List<Item> itemList = itemRepository.findByFamilyAndProjectThing(family, roomInterior.getThing());
                for (Item availableItem : itemList) {
                    if (availableItem.getPlace().equals(ItemPlace.home) && availableItem.getInteriorId() == roomInterior.getId()) {
                        currentItem = availableItem;
                    }
                    if (availableItem.getPlace().equals(ItemPlace.storage)) {
                        availableItems.add(availableItem);
                    }
                }
                roomInteriorWithItems.setCurrentItem(currentItem);
                roomInteriorWithItems.setAvailableItems(availableItems);
                roomInteriorWithItems.setKnownThing(family.getCraftThings().contains(roomInterior.getThing()));
                houseInterior.addRoomInterior(room, roomInteriorWithItems);

                if (currentItem == null) {
                    allItems = false;
                }
            }

        }
        houseInterior.setFull(allItems);

        if (house.nextHouse()) {
            houseInterior.setNextHouse(getNextHouse(house));
        }

        return houseInterior;
    }

    public House getNextHouse(House house) {
        return houseRepository.findOne(house.getId() + 1);
    }

    public boolean setItemToRoomInterior(Family family, Long itemId, Long roomInteriorId) {
        Item item = itemRepository.findByFamilyAndId(family, itemId);
        if (item == null) {
            logger.error(family.logName() + "want to set non-existing item to roomInterior: item.id=" + itemId);
            return false;
        }
        RoomInterior roomInterior = roomInteriorRepository.findOne(roomInteriorId);
        if (roomInterior == null) {
            logger.error(family.logName() + "want to set item to non-existing roomInterior: roomInterior.id=" + roomInteriorId);
            return false;
        }

        Item oldItem = itemRepository.findByFamilyAndInteriorId(family, roomInterior.getId());
        if (oldItem == null) {
            logger.debug(family.logName() + "has no item with interiorId=" + roomInterior.getId());
        } else {
            oldItem.setPlace(ItemPlace.storage);
            oldItem.setInteriorId(0L);
            logger.debug(family.logName() + "has item with interiorId=" + roomInterior.getId() + ". Put to storage and set interiorId to 0");
            itemRepository.save(oldItem);
        }
        item.setPlace(ItemPlace.home);
        item.setInteriorId(roomInterior.getId());
        logger.debug(family.logName() + "set item " + item.getProject().getName() + "(" + item.getId() + ") to room interior thing=" + roomInterior.getThing().getName());
        itemRepository.save(item);
        return true;
    }

    public boolean unsetItem(Family family, Long itemId) {
        Item item = itemRepository.findByFamilyAndId(family, itemId);
        if (item == null) {
            logger.error(family.logName() + "want to unset non-existing item: " + itemId);
            return false;
        }
        if (item.getPlace().equals(ItemPlace.storage)) {
            logger.error(family.logName() + "want to unset item which is already in storage: " + item.getProject().getName() + "(" + item.getId() + ")");
            return false;
        }

        if (item.getPlace().equals(ItemPlace.store)) {
            item.setPlace(ItemPlace.storage);
            item.setCost(0);
            logger.debug(family.logName() + "put item " + item.getProject().getName() + "(" + item.getId() + ") back from store to storage");
            itemRepository.save(item);
            return true;
        }

        RoomInterior roomInterior = roomInteriorRepository.findOne(item.getInteriorId());
        if (roomInterior == null) {
            logger.error(family.logName() + "want to unset item from non-existing roomInterior: " + item.getProject().getName() + "(" + item.getId() + "), roomInterior.id=" + item.getInteriorId());
            return false;
        }

        item.setPlace(ItemPlace.storage);
        item.setInteriorId(0L);
        logger.debug(family.logName() + "unset item " + item.getProject().getName() + "(" + item.getId() + ") from room interior thing=" + roomInterior.getThing().getName());
        itemRepository.save(item);
        return true;
    }

    public List<Item> getItemsInStorage(Family family) {
        return itemRepository.findByFamilyAndPlaceOrderByProjectThing(family, ItemPlace.storage);
    }

    public List<Item> getItemsInStore(Family family) {
        return itemRepository.findByFamilyAndPlaceOrderByProjectThing(family, ItemPlace.store);
    }

    public Item getItem(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public List<Item> getItemsInStoreByThing(Thing thing) {
        return itemRepository.findByPlaceAndProjectThing(ItemPlace.store, thing);
    }

    public Thing getThing(Long thingId) {
        return thingRepository.findOne(thingId);
    }

    public List<Room> getRoomsByHouseId(Long houseId) {
        List<Room> rooms = roomRepository.findByHouseIdLessThanEqualOrderById(houseId);
        return rooms;
    }

    public List<RoomInterior> getRoomInteriorByRoomIdAndHouseId(Long roomId, Long houseId) {
        List<RoomInterior> roomInteriorList = roomInteriorRepository.findByHouseIdLessThanEqualAndRoomIdOrderById(houseId, roomId);
        return roomInteriorList;
    }

    public List<House> getHouseList() {
        return (List<House>) houseRepository.findAll();
    }
}
