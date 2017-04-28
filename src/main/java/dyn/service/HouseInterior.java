package dyn.service;

import dyn.model.Family;
import dyn.model.House;
import dyn.model.Room;
import dyn.model.RoomThingWithItems;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by OM on 30.03.2017.
 */
public class HouseInterior {
    private Family family;
    private House house;
    private House nextHouse = null;
    private Map<Room, List<RoomThingWithItems>> roomList = new LinkedHashMap<>();
    private boolean full;

    public void addRoomInterior(Room room, RoomThingWithItems roomInterior) {
        if (roomList.containsKey(room)) {
            roomList.get(room).add(roomInterior);
        } else {
            ArrayList<RoomThingWithItems> roomInteriors = new ArrayList<>();
            roomInteriors.add(roomInterior);
            roomList.put(room, roomInteriors);
        }
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public House getNextHouse() {
        return nextHouse;
    }

    public void setNextHouse(House nextHouse) {
        this.nextHouse = nextHouse;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public Map<Room, List<RoomThingWithItems>> getRoomList() {
        return roomList;
    }


}
