package dyn.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OM on 27.04.2017.
 */
public class RoomView {
    private Room room;
    private List<RoomThingWithItems> roomThingWithItemsList = new ArrayList<>();
    private List<RoomThingWithProjects> roomThingWithProjects = new ArrayList<>();
    private boolean full;

    private String backgroundUrl;

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

    public List<RoomThingWithProjects> getRoomThingWithProjects() {
        return roomThingWithProjects;
    }

    public void setRoomThingWithProjects(List<RoomThingWithProjects> roomThingWithProjects) {
        this.roomThingWithProjects = roomThingWithProjects;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public String getBackgroundUrl() {
        backgroundUrl = "/graphics/" + room.getBackground();

        return backgroundUrl;
    }

    public int getSumOfCraftPoints() {
        List<Thing> list = new ArrayList<>();
        int sum = 0;
        for (RoomThingWithItems roomThingWithItems : roomThingWithItemsList) {
            Thing thing = roomThingWithItems.getRoomThing().getThing();
            if (!list.contains(thing)) {
                list.add(thing);
                sum = sum + thing.getCost();
            }
        }
        return sum;
    }
}
