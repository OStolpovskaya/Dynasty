package dyn.model.buildings;

import dyn.model.Thing;

import javax.persistence.*;

/**
 * Created by OM on 26.04.2017.
 */
@Entity
@Table(name = "building_thing")
public class BuildingThing {
    private Long id;
    private Building building;
    private Thing thing;
    private int x;
    private int y;
    private int layer;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    @ManyToOne
    public Thing getThing() {
        return thing;
    }

    public void setThing(Thing thing) {
        this.thing = thing;
    }

    @Basic
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Basic
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Basic
    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }


}
