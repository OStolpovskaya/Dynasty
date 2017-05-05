package dyn.service;

import dyn.model.*;
import dyn.repository.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by OM on 30.03.2017.
 */
@Service
public class HouseService {

    private static final Logger logger = LogManager.getLogger(HouseService.class);
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomThingRepository roomThingRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ThingRepository thingRepository;

    public List<RoomView> getRoomMaps(House house, Family family) {
        List<RoomView> roomViewList = new ArrayList<>();

        List<Room> roomList;
        if (house.getType() == HouseType.home) {
            roomList = roomRepository.findByHouseIdLessThanEqualOrderById(house.getId());
        } else {
            roomList = roomRepository.findByHouseIdOrderById(house.getId());
        }
        for (Room room : roomList) {
            RoomView roomView = new RoomView(room);
            roomView.setFull(true);

            List<RoomThing> roomThings;

            ItemPlace itemPlace;
            if (house.getType() == HouseType.home) {
                roomThings = roomThingRepository.findByHouseIdLessThanEqualAndRoomIdOrderById(house.getId(), room.getId());
                itemPlace = ItemPlace.home;
            } else {
                roomThings = roomThingRepository.findByHouseIdAndRoomIdOrderById(house.getId(), room.getId());
                itemPlace = ItemPlace.building;
            }
            for (RoomThing roomThing : roomThings) {
                RoomThingWithItems roomThingWithItems = new RoomThingWithItems(roomThing);

                Item currentItem = null;
                List<Item> availableItems = new ArrayList<>();

                List<Item> itemList = itemRepository.findByFamilyAndProjectThing(family, roomThing.getThing());
                for (Item availableItem : itemList) {
                    if (availableItem.getPlace().equals(itemPlace) && Objects.equals(availableItem.getInteriorId(), roomThing.getId())) {
                        currentItem = availableItem;
                    }
                    if (availableItem.getPlace().equals(ItemPlace.storage)) {
                        availableItems.add(availableItem);
                    }
                }
                roomThingWithItems.setCurrentItem(currentItem);
                roomThingWithItems.setAvailableItems(availableItems);
                roomThingWithItems.setKnownThing(family.getCraftThings().contains(roomThing.getThing()));

                roomView.getRoomThingWithItemsList().add(roomThingWithItems);
                if (currentItem == null) {
                    roomView.setFull(false);
                }
            }

            roomViewList.add(roomView);
        }
        return roomViewList;
    }

    public HouseInterior getHouseInterior(Family family) {

        House house = family.getHouse();

        HouseInterior houseInterior = new HouseInterior();
        houseInterior.setFamily(family);
        houseInterior.setHouse(house);

        boolean allItems = true;
        List<Room> rooms = roomRepository.findByHouseIdLessThanEqualOrderById(house.getId());
        for (Room room : rooms) {
            List<RoomThing> interiorList = roomThingRepository.findByHouseIdLessThanEqualAndRoomIdOrderById(house.getId(), room.getId());
            for (RoomThing roomThing : interiorList) {
                RoomThingWithItems roomThingWithItems = new RoomThingWithItems(roomThing);

                Item currentItem = null;
                List<Item> availableItems = new ArrayList<>();

                List<Item> itemList = itemRepository.findByFamilyAndProjectThing(family, roomThing.getThing());
                for (Item availableItem : itemList) {
                    if (availableItem.getPlace().equals(ItemPlace.home) && availableItem.getInteriorId() == roomThing.getId()) {
                        currentItem = availableItem;
                    }
                    if (availableItem.getPlace().equals(ItemPlace.storage)) {
                        availableItems.add(availableItem);
                    }
                }
                roomThingWithItems.setCurrentItem(currentItem);
                roomThingWithItems.setAvailableItems(availableItems);
                roomThingWithItems.setKnownThing(family.getCraftThings().contains(roomThing.getThing()));
                houseInterior.addRoomInterior(room, roomThingWithItems);

                if (currentItem == null) {
                    allItems = false;
                }
            }

        }
        houseInterior.setFull(allItems);

        if (house.hasNextLevel()) {
            houseInterior.setNextHouse(getNextHouse(house));
        }

        return houseInterior;
    }

    public House getNextHouse(House house) {
        return houseRepository.findOne(house.getId() + 1);
    }

    public List<Item> getItemsInStorage(Family family) {
        return itemRepository.findByFamilyAndPlaceAndProjectThingCraftBranchIdLessThanEqualOrderByProjectThing(family, ItemPlace.storage, Const.MEAL);
//        return itemRepository.findByFamilyAndPlaceOrderByProjectThing(family, ItemPlace.storage);
    }

    public List<Item> getBuffsInStorage(Family family) {
        return itemRepository.findByFamilyAndPlaceAndProjectThingCraftBranchIdOrderByProjectThingAscProjectAsc(family, ItemPlace.storage, Const.SERVICE_AND_BUFFS);
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

    public List<RoomThing> getRoomInteriorByRoomIdAndHouseId(Long roomId, Long houseId) {
        List<RoomThing> roomThingList = roomThingRepository.findByHouseIdLessThanEqualAndRoomIdOrderById(houseId, roomId);
        return roomThingList;
    }

    public List<House> getHomeList() {
        return houseRepository.findAllByType(HouseType.home);
    }


    public List<House> getBuildingList() {
        return houseRepository.findAllByType(HouseType.building);
    }

    public House getHouse(Long buildingId) {
        return houseRepository.findOne(buildingId);
    }

    public Item getItemByFamilyAndItemId(Family family, Long itemId) {
        return itemRepository.findByFamilyAndId(family, itemId);
    }

    public RoomThing getRoomThingById(Long roomThingId) {
        return roomThingRepository.findOne(roomThingId);
    }

    public Item getItemByFamilyAndRoomThing(Family family, RoomThing roomThing) {
        return itemRepository.findByFamilyAndInteriorId(family, roomThing.getId());
    }

    public void saveRoomThing(RoomThing roomThing) {
        roomThingRepository.save(roomThing);
    }

    public void changeRoomThing(Long roomThingId, Long roomThingHouseId, int roomThingX, int roomThingY, int roomThingLayer) {
        RoomThing roomThing = roomThingRepository.findOne(roomThingId);
        roomThing.setHouse(houseRepository.findOne(roomThingHouseId));
        roomThing.setX(roomThingX);
        roomThing.setY(roomThingY);
        roomThing.setLayer(roomThingLayer);
        roomThingRepository.save(roomThing);
    }

    public RoomThing newRoomThing(Long thingId, Long houseId, Long roomId, int x, int y, int layer) {
        RoomThing roomThing = new RoomThing();
        roomThing.setThing(thingRepository.findOne(thingId));
        roomThing.setRoom(roomRepository.findOne(roomId));
        roomThing.setHouse(houseRepository.findOne(houseId));
        roomThing.setX(x);
        roomThing.setY(y);
        roomThing.setLayer(layer);
        roomThingRepository.save(roomThing);

        return roomThing;
    }

    public House getBuildingByProduction(Project project) {
        return houseRepository.findByProduction(project);
    }

    public void deleteItem(Item item) {
        itemRepository.delete(item);
    }
}
