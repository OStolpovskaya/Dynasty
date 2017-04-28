package dyn.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by OM on 21.02.2017.
 */
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(name = "house_id")
    private long houseId;

    //==============================================================
    @OneToMany(mappedBy = "room")
    private List<RoomThing> roomThings;
    //==============================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getHouseId() {
        return houseId;
    }

    public void setHouseId(long houseId) {
        this.houseId = houseId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Room{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", houseId=").append(houseId);
        sb.append('}');
        return sb.toString();
    }
}
