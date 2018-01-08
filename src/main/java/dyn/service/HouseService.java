package dyn.service;

import dyn.model.*;
import dyn.repository.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

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
    private FamilyBuildingRepository familyBuildingRepository;

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
                roomThings = roomThingRepository.findByHouseIdLessThanEqualAndRoomIdOrderByLayerAsc(house.getId(), room.getId());
                itemPlace = ItemPlace.home;
            } else {
                roomThings = roomThingRepository.findByHouseIdAndRoomIdOrderByLayerAsc(house.getId(), room.getId());
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

    public House getNextHouse(House house) {
        return houseRepository.findOne(house.getId() + 1);
    }

    public List<Item> getItemsInStorage(Family family) {
        return itemRepository.findByFamilyAndPlaceAndProjectThingCraftBranchIdLessThanEqualOrderByProjectThingAscProjectAscQualityAsc(family, ItemPlace.storage, Const.CRAFTBRANCH_MAX);
    }

    public List<Item> getBuffsInStorage(Family family) {
        return itemRepository.findByFamilyAndPlaceAndProjectThingCraftBranchIdOrderByProjectThingAscProjectAsc(family, ItemPlace.storage, Const.CRAFTBRANCH_SERVICE_AND_BUFFS);
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
        return itemRepository.findByPlaceAndProjectThingOrderByProjectAscQualityAscCostAsc(ItemPlace.store, thing);
    }

    public Thing getThing(Long thingId) {
        return thingRepository.findOne(thingId);
    }

    public List<Room> getRoomsByHouseId(Long houseId) {
        List<Room> rooms = roomRepository.findByHouseIdLessThanEqualOrderById(houseId);
        return rooms;
    }

    public List<RoomThing> getRoomInteriorByRoomIdAndHouseId(Long roomId, Long houseId) {
        List<RoomThing> roomThingList = roomThingRepository.findByHouseIdLessThanEqualAndRoomIdOrderByLayerAsc(houseId, roomId);
        return roomThingList;
    }

    public List<House> getHomeList() {
        return houseRepository.findAllByType(HouseType.home);
    }


    public List<House> getBuildingList() {
        return houseRepository.findAllByTypeOrderByPairsNum(HouseType.building);
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

    public List<Item> getItemsInStoreByProject(Project project) {
        return itemRepository.findByPlaceAndProject(ItemPlace.store, project);
    }

    public void updateBuildingQuality(Family family, House building, float buildingQuality) {
        FamilyBuilding familyBuilding = familyBuildingRepository.findByFamilyAndBuilding(family, building);
        familyBuilding.setBuildingQuality(buildingQuality);
        familyBuildingRepository.save(familyBuilding);
    }

    public List<FamilyBuilding> getFamilyBuildings(Family family) {
        return familyBuildingRepository.findByFamilyOrderByBuildingPairsNum(family);
    }

    public FamilyBuilding getFamilyBuildingByFamilyAndBuilding(Family family, House building) {
        return familyBuildingRepository.findByFamilyAndBuilding(family, building);
    }

    public float countHouseQualitySalaryCoeff(float houseQuality) {
        float houseInc = 0;
        if (1 <= houseQuality && houseQuality < 2) {
            houseInc = 0.1f;
        } else if (2 <= houseQuality && houseQuality < 3) {
            houseInc = 0.2f;
        } else if (3 <= houseQuality && houseQuality < 4) {
            houseInc = 0.3f;
        } else if (4 <= houseQuality && houseQuality < 5) {
            houseInc = 0.4f;
        } else if (5 <= houseQuality) {
            houseInc = 0.5f;
        }

        return houseInc;
    }

    public float countHouseQualityFertilitySub(float houseQuality) {
        float houseInc = 0;
        if (1 <= houseQuality && houseQuality < 2) {
            houseInc = 0.02f;
        } else if (2 <= houseQuality && houseQuality < 3) {
            houseInc = 0.04f;
        } else if (3 <= houseQuality && houseQuality < 4) {
            houseInc = 0.06f;
        } else if (4 <= houseQuality && houseQuality < 5) {
            houseInc = 0.08f;
        } else if (5 <= houseQuality) {
            houseInc = 0.10f;
        }

        return houseInc;
    }

    public void removeRoomThing(Long roomThingId) {
        List<Item> items = itemRepository.findByInteriorId(roomThingId);
        for (Item item : items) {
            item.setInteriorId(0L);
            item.setPlace(ItemPlace.storage);
            itemRepository.save(item);
        }
        roomThingRepository.delete(roomThingId);
    }

    public List<Item> getBuffsInStorageByProject(Family family, Long projectId) {
        return itemRepository.findByFamilyAndPlaceAndProjectId(family, ItemPlace.storage, projectId);
    }

    public List<RoomThing> getRoomThingByHouse(House house) {
        return roomThingRepository.findAllByHouseOrderByRoomAscThingAsc(house);
    }

    public Map<String, Integer> getHouseStatistics() {

        Map<String, Integer> map = new LinkedHashMap<>();
        List<Object[]> list = houseRepository.countHouses();
        for (Object[] objects : list) {
            String houseName = (String) objects[0];
            BigInteger count = (BigInteger) objects[1];
            map.put(houseName, count.intValue());

        }
        return map;
    }

    public Map<String, Integer> getBuildingStatistics() {
        Map<String, Integer> map = new LinkedHashMap<>();
        List<Object[]> list = houseRepository.countBuildings();
        for (Object[] objects : list) {
            String houseName = (String) objects[0];
            BigInteger count = (BigInteger) objects[1];
            map.put(houseName, count.intValue());

        }
        return map;
    }
}
