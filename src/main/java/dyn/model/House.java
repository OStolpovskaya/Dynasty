package dyn.model;

import javax.persistence.*;

/**
 * Created by OM on 21.02.2017.
 */
@Entity
@Table(name = "house")
public class House {
    private Long id;
    private HouseType type;
    private String name;
    private int cost;
    private String desc;
    private int pairsNum;
    private int fianceeNum;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    public HouseType getType() {
        return type;
    }

    public void setType(HouseType houseType) {
        this.type = houseType;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPairsNum() {
        return pairsNum;
    }

    public void setPairsNum(int pairs_num) {
        this.pairsNum = pairs_num;
    }

    public int getFianceeNum() {
        return fianceeNum;
    }

    public void setFianceeNum(int fiancee_num) {
        this.fianceeNum = fiancee_num;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("House{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public boolean hasNextLevel() {
        return id < 10;
    }
}
