package dyn.model.career;

import dyn.utils.ResUtils;
import dyn.utils.ResourcesHolder;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "vocation")
public class Vocation implements ResourcesHolder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private int startSalary;

    private int food;
    private int wood;
    private int metall;
    private int plastic;
    private int microelectronics;
    private int cloth;
    private int stone;
    private int chemical;

    @OneToMany(mappedBy = "vocation")
    private List<Profession> professionList;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartSalary() {
        return startSalary;
    }

    public void setStartSalary(int startSalary) {
        this.startSalary = startSalary;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
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

    public String resString(int coeff) {

        return ResUtils.getResString(this, coeff);
    }

    public List<Profession> getProfessionList() {
        return professionList;
    }
}