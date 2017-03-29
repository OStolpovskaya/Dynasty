package dyn.model;

import javax.persistence.*;

/**
 * Created by OM on 21.02.2017.
 */
@Entity
@Table(name = "room_interior")
public class RoomInterior {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Room room;

    @OneToOne
    private Thing thing;

    @OneToOne
    private House house;

    private int x;

    private int y;

    private int layer;

}
