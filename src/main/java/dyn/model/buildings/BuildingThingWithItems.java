package dyn.model.buildings;

import dyn.model.Item;
import dyn.model.Thing;

import java.util.List;

/**
 * Created by OM on 26.04.2017.
 */
public class BuildingThingWithItems {
    private BuildingThing buildingThing;
    private Building building;
    private Thing thing;
    private boolean knownThing = false;
    private Item currentItem;
    private List<Item> availableItems;

    public BuildingThingWithItems(BuildingThing buildingThing) {
        this.buildingThing = buildingThing;
        building = buildingThing.getBuilding();
        thing = buildingThing.getThing();
    }

    public BuildingThing getBuildingThing() {
        return buildingThing;
    }

    public void setBuildingThing(BuildingThing buildingThing) {
        this.buildingThing = buildingThing;
    }

    public boolean isKnownThing() {
        return knownThing;
    }

    public void setKnownThing(boolean knownThing) {
        this.knownThing = knownThing;
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

    public Building getBuilding() {
        return building;
    }

    public Thing getThing() {
        return thing;
    }
}
