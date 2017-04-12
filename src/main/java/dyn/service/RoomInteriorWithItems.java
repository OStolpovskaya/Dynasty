package dyn.service;

import dyn.model.Item;
import dyn.model.RoomInterior;

import java.util.List;

/**
 * Created by OM on 30.03.2017.
 */
public class RoomInteriorWithItems {
    private RoomInterior roomInterior;
    private boolean knownThing = false;
    private Item currentItem;
    private List<Item> availableItems;

    public RoomInteriorWithItems(RoomInterior roomInterior) {
        this.roomInterior = roomInterior;
    }

    public RoomInterior getRoomInterior() {
        return roomInterior;
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
