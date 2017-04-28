package dyn.model;

import javax.persistence.*;

/**
 * Created by OM on 21.02.2017.
 */
@Entity
@Table(name = "room_thing")
public class RoomThing {
    private Long id;
    private Room room;
    private Thing thing;
    private House house;
    private int x;
    private int y;
    private int layer;

    //===========================================================
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public Room getRoom() {
        return room;
    }
    public void setRoom(Room room) {
        this.room = room;
    }

    @OneToOne
    public Thing getThing() {
        return thing;
    }
    public void setThing(Thing thing) {
        this.thing = thing;
    }

    @ManyToOne
    public House getHouse() {
        return house;
    }
    public void setHouse(House house) {
        this.house = house;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public int getLayer() {
        return layer;
    }
    public void setLayer(int layer) {
        this.layer = layer;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RoomThing{");
        sb.append("id=").append(id);
        sb.append(", room=").append(room.getName());
        sb.append(", thing=").append(thing.getName());
        sb.append(", house=").append(house.getName());
        sb.append(", x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", layer=").append(layer);
        sb.append('}');
        return sb.toString();
    }
}
