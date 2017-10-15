package dyn.model;

import dyn.form.RaceAppearanceForm;
import dyn.utils.ResUtils;
import dyn.utils.ResourcesHolder;

import javax.persistence.*;
import java.util.List;

/**
 * Created by OM on 21.02.2017.
 */
@Entity
@Table(name = "race")
public class Race implements ResourcesHolder {
    public static final long RACE_HUMAN = 1;
    public static final long RACE_GM_HUMAN = 2;
    public static final long RACE_HIGH = 6;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private int food;
    private int wood;
    private int metall;
    private int plastic;
    private int microelectronics;
    private int cloth;
    private int stone;
    private int chemical;

    // ===================================
    @OneToMany(mappedBy = "race")
    private List<RaceAppearance> raceAppearanceList;

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

    public List<RaceAppearance> getRaceAppearanceList() {
        return raceAppearanceList;
    }

    public void setRaceAppearanceList(List<RaceAppearance> raceAppearanceList) {
        this.raceAppearanceList = raceAppearanceList;
    }

    public RaceAppearanceForm getSummary() {
        RaceAppearanceForm summary = new RaceAppearanceForm();
        for (RaceAppearance raceAppearance : raceAppearanceList) {
            if (raceAppearance.getBody() != null && !summary.getBodyList().contains(raceAppearance.getBody())) summary.getBodyList().add(raceAppearance.getBody());
            if (raceAppearance.getEars() != null && !summary.getEarsList().contains(raceAppearance.getEars())) summary.getEarsList().add(raceAppearance.getEars());
            if (raceAppearance.getEyebrows() != null && !summary.getEyebrowsList().contains(raceAppearance.getEyebrows())) summary.getEyebrowsList().add(raceAppearance.getEyebrows());
            if (raceAppearance.getEyeColor() != null && !summary.getEyeColorList().contains(raceAppearance.getEyeColor())) summary.getEyeColorList().add(raceAppearance.getEyeColor());
            if (raceAppearance.getEyes() != null && !summary.getEyesList().contains(raceAppearance.getEyes())) summary.getEyesList().add(raceAppearance.getEyes());
            if (raceAppearance.getHairColor() != null && !summary.getHairColorList().contains(raceAppearance.getHairColor())) summary.getHairColorList().add(raceAppearance.getHairColor());
            if (raceAppearance.getHairType() != null && !summary.getHairTypeList().contains(raceAppearance.getHairType())) summary.getHairTypeList().add(raceAppearance.getHairType());
            if (raceAppearance.getHead() != null && !summary.getHeadList().contains(raceAppearance.getHead())) summary.getHeadList().add(raceAppearance.getHead());
            if (raceAppearance.getHeight() != null && !summary.getHeightList().contains(raceAppearance.getHeight())) summary.getHeightList().add(raceAppearance.getHeight());
            if (raceAppearance.getMouth() != null && !summary.getMouthList().contains(raceAppearance.getMouth())) summary.getMouthList().add(raceAppearance.getMouth());
            if (raceAppearance.getNose() != null && !summary.getNoseList().contains(raceAppearance.getNose())) summary.getNoseList().add(raceAppearance.getNose());
            if (raceAppearance.getSkinColor() != null && !summary.getSkinColorList().contains(raceAppearance.getSkinColor())) summary.getSkinColorList().add(raceAppearance.getSkinColor());
        }
        return summary;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Race{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String resString() {
        return ResUtils.getResString(this);
    }
}
