package dyn.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OM on 30.03.2017.
 */
public class RoomThingWithItems {
    private RoomThing roomThing;
    private boolean knownThing = false;
    private Item currentItem;
    private List<Item> availableItems;

    public RoomThingWithItems(RoomThing roomThing) {
        this.roomThing = roomThing;
    }

    public RoomThing getRoomThing() {
        return roomThing;
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
    }

    public List<Item> getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(List<Item> availableItems) {
        this.availableItems = availableItems;
    }

    public boolean isKnownThing() {
        return knownThing;
    }

    public void setKnownThing(boolean knownThing) {
        this.knownThing = knownThing;
    }

    public String getStringForModal() {
        StringBuilder sb = new StringBuilder();
        sb.append(roomThing.getThing().getId()).append(",");
        sb.append("'").append(roomThing.getThing().getName()).append("',");
        sb.append(roomThing.getId()).append(",");
        sb.append(roomThing.getThing().getWidth()).append(",");
        sb.append(roomThing.getThing().getHeight()).append(",");
        sb.append(roomThing.getLayer()).append(",");
        sb.append(knownThing).append(",");
        sb.append(currentItem == null ? "0" : currentItem.getId()).append(",");
        sb.append("'").append(currentItem == null ? "Нет вещи" : currentItem.shortTitle()).append("',");
        sb.append(currentItem == null ? "0" : currentItem.getX()).append(",");
        sb.append(currentItem == null ? "0" : currentItem.getY()).append(",");
        sb.append(currentItem == null ? "0" : currentItem.getLayer()).append(",");
        List<String> availableItemIds = new ArrayList<>();
        List<String> availableItemNames = new ArrayList<>();
        for (Item availableItem : availableItems) {
            availableItemIds.add(availableItem.getId().toString());
            availableItemNames.add("'" + availableItem.shortTitle() + "'");
        }
        sb.append("[").append(String.join(",", availableItemNames)).append("],");
        sb.append("[").append(String.join(",", availableItemIds)).append("]");
        return sb.toString();
        /*
        .getRoomThing().getThing().getId()}+','+
        + '\'' + ${roomThingWithItems.getRoomThing().getThing().getName()} + '\','+
        + ${roomThingWithItems.getRoomThing().getId()}+','+
        + ${roomThingWithItems.isKnownThing()}+','+
        + ${roomThingWithItems.isKnownThing()}+','+
        ');'">*/
    }
}
