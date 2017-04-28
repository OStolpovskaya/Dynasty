package dyn.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OM on 27.04.2017.
 */
public class RoomView {
    private Room room;
    private List<RoomThingWithItems> roomThingWithItemsList = new ArrayList<>();
    private boolean full;

    public RoomView(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public List<RoomThingWithItems> getRoomThingWithItemsList() {
        return roomThingWithItemsList;
    }

    public void setRoomThingWithItemsList(List<RoomThingWithItems> roomThingWithItemsList) {
        this.roomThingWithItemsList = roomThingWithItemsList;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }
}
