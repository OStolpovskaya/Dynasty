package dyn.model;

import dyn.utils.ResourcesHolder;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by OM on 07.04.2017.
 */
@Entity
@Table(name = "family_log")
public class FamilyLog implements ResourcesHolder {
    private int id;
    private Family family;
    private int level;
    private String text;

    private int money;
    private int food;
    private int wood;
    private int metall;
    private int plastic;
    private int microelectronics;
    private int cloth;
    private int stone;
    private int chemical;
    private int craftpoint;

    public FamilyLog() {
    }

    public FamilyLog(Family family) {
        this.family = family;
        this.level = family.getLevel();
        this.text = "";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    @Column(name = "level")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Column(name = "text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void addText(String mess) {
        text = new SimpleDateFormat().format(new Date()) + " " + mess + "<br>" + text;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public int getFood() {
        return food;
    }

    @Override
    public void setFood(int food) {
        this.food = food;
    }

    @Override
    public int getWood() {
        return wood;
    }

    @Override
    public void setWood(int wood) {
        this.wood = wood;
    }

    @Override
    public int getMetall() {
        return metall;
    }

    @Override
    public void setMetall(int metall) {
        this.metall = metall;
    }

    @Override
    public int getPlastic() {
        return plastic;
    }

    @Override
    public void setPlastic(int plastic) {
        this.plastic = plastic;
    }

    @Override
    public int getMicroelectronics() {
        return microelectronics;
    }

    @Override
    public void setMicroelectronics(int microelectronics) {
        this.microelectronics = microelectronics;
    }

    @Override
    public int getCloth() {
        return cloth;
    }

    @Override
    public void setCloth(int cloth) {
        this.cloth = cloth;
    }

    @Override
    public int getStone() {
        return stone;
    }

    @Override
    public void setStone(int stone) {
        this.stone = stone;
    }

    @Override
    public int getChemical() {
        return chemical;
    }

    @Override
    public void setChemical(int chemical) {
        this.chemical = chemical;
    }

    public int getCraftpoint() {
        return craftpoint;
    }

    public void setCraftpoint(int craftpoint) {
        this.craftpoint = craftpoint;
    }
}
