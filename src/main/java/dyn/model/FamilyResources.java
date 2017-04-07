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
    private int wood;
    private int metall;
    private int plastic;
    private int microelectronics;
    private int cloth;
    private int stone;
    private int chemical;
    private Family family;

    public FamilyResources() {
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
        wood += vocation.getWood() * professionLevel;
        metall += vocation.getMetall() * professionLevel;
        plastic += vocation.getPlastic() * professionLevel;
        microelectronics += vocation.getMicroelectronics() * professionLevel;
        cloth += vocation.getCloth() * professionLevel;
        stone += vocation.getStone() * professionLevel;
        chemical += vocation.getChemical() * professionLevel;
    }

    public String resString() {
        return ResourcesUtils.getResString(this);
    }
}
