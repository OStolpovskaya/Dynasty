package dyn.model;

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
}
