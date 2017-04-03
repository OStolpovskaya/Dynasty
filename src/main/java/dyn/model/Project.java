package dyn.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by OM on 21.02.2017.
 */
@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Thing thing;

    private String name;

    private int cost;

    @Lob
    private byte[] view;

    // =================================================
    @OneToMany(mappedBy = "project")
    private List<Item> items;

    // =================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Thing getThing() {
        return thing;
    }

    public void setThing(Thing thing) {
        this.thing = thing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public byte[] getView() {
        return view;
    }

    public void setView(byte[] view) {
        this.view = view;
    }
}
