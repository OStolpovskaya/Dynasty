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

    private int wood;
    private int metall;
    private int plastic;
    private int microelectronics;
    private int cloth;
    private int stone;
    private int chemical;

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

    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }

    public int getMetall() {
        return metall;
    }

    public void setMetall(int metall) {
        this.metall = metall;
    }

    public int getPlastic() {
        return plastic;
    }

    public void setPlastic(int plastic) {
        this.plastic = plastic;
    }

    public int getMicroelectronics() {
        return microelectronics;
    }

    public void setMicroelectronics(int microelectronics) {
        this.microelectronics = microelectronics;
    }

    public int getCloth() {
        return cloth;
    }

    public void setCloth(int cloth) {
        this.cloth = cloth;
    }

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public int getChemical() {
        return chemical;
    }

    public void setChemical(int chemical) {
        this.chemical = chemical;
    }
}
