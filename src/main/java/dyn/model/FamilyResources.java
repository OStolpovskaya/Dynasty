package dyn.model;

import dyn.model.career.Career;
import dyn.model.career.Vocation;
import dyn.utils.ResourcesHolder;
import dyn.utils.ResourcesUtils;

import javax.persistence.*;

/**
 * Created by OM on 04.04.2017.
 */
@Entity
@Table(name = "family_resources", schema = "dyn")
public class FamilyResources implements ResourcesHolder {
    public static final int BUYCOST = 500;

    private Long id;
    private int food;
    private int wood;
    private int metall;
    private int plastic;
    private int microelectronics;
    private int cloth;
    private int stone;
    private int chemical;
    private Family family;

    public FamilyResources() {
        food = 10;
        wood = 10;
        metall = 10;
        plastic = 10;
        microelectronics = 10;
        cloth = 10;
        stone = 10;
        chemical = 10;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    @Basic
    @Column(name = "wood")
    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }

    @Basic
    @Column(name = "metall")
    public int getMetall() {
        return metall;
    }

    public void setMetall(int metall) {
        this.metall = metall;
    }

    @Basic
    @Column(name = "plastic")
    public int getPlastic() {
        return plastic;
    }

    public void setPlastic(int plastic) {
        this.plastic = plastic;
    }

    @Basic
    @Column(name = "microelectronics")
    public int getMicroelectronics() {
        return microelectronics;
    }

    public void setMicroelectronics(int microelectronics) {
        this.microelectronics = microelectronics;
    }

    @Basic
    @Column(name = "cloth")
    public int getCloth() {
        return cloth;
    }

    public void setCloth(int cloth) {
        this.cloth = cloth;
    }

    @Basic
    @Column(name = "stone")
    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    @Basic
    @Column(name = "chemical")
    public int getChemical() {
        return chemical;
    }

    public void setChemical(int chemical) {
        this.chemical = chemical;
    }

    @OneToOne(mappedBy = "familyResources")
    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public void makingProject(Project project) {
        food -= project.getFood();
        wood -= project.getWood();
        metall -= project.getMetall();
        plastic -= project.getPlastic();
        microelectronics -= project.getMicroelectronics();
        cloth -= project.getCloth();
        stone -= project.getStone();
        chemical -= project.getChemical();
    }

    public boolean addRes(String res, int amount) {
        switch (res) {
            case "food":
                food += amount;
                break;
            case "wood":
                wood += amount;
                break;
            case "metall":
                metall += amount;
                break;
            case "plastic":
                plastic += amount;
                break;
            case "microelectronics":
                microelectronics += amount;
                break;
            case "cloth":
                cloth += amount;
                break;
            case "stone":
                stone += amount;
                break;
            case "chemical":
                chemical += amount;
                break;
            default:
                return false;
        }
        return true;
    }

    public void addResFromVocation(Career career) {
        Vocation vocation = career.getVocation();
        int professionLevel = career.getProfession().getLevel();
        food += vocation.getFood() * professionLevel;
        wood += vocation.getWood() * professionLevel;
        metall += vocation.getMetall() * professionLevel;
        plastic += vocation.getPlastic() * professionLevel;
        microelectronics += vocation.getMicroelectronics() * professionLevel;
        cloth += vocation.getCloth() * professionLevel;
        stone += vocation.getStone() * professionLevel;
        chemical += vocation.getChemical() * professionLevel;
    }

    public void addResFromRace(Race race) {
        food += race.getFood();
        wood += race.getWood();
        metall += race.getMetall();
        plastic += race.getPlastic();
        microelectronics += race.getMicroelectronics();
        cloth += race.getCloth();
        stone += race.getStone();
        chemical += race.getChemical();
    }

    public String resString() {
        return ResourcesUtils.getResString(this);
    }

    public void addWood(int num) {
        wood += num;
    }

    public void addFood(int num) {
        food += num;
    }

    public void addMetall(int num) {
        metall += num;
    }

    public void addPlastic(int num) {
        plastic += num;
    }

    public void addMicroelectronics(int num) {
        microelectronics += num;
    }

    public void addCloth(int num) {
        cloth += num;
    }

    public void addStone(int num) {
        stone += num;
    }

    public void addChemical(int num) {
        chemical += num;
    }


    public void addResFromDestroyedItem(Item item) {
        Project project = item.getProject();
        food += Math.ceil(project.getFood() * 0.5);
        wood += Math.ceil(project.getWood() * 0.5);
        metall += Math.ceil(project.getMetall() * 0.5);
        plastic += Math.ceil(project.getPlastic() * 0.5);
        microelectronics += Math.ceil(project.getMicroelectronics() * 0.5);
        cloth += Math.ceil(project.getCloth() * 0.5);
        stone += Math.ceil(project.getStone() * 0.5);
        chemical += Math.ceil(project.getChemical() * 0.5);
    }
}
