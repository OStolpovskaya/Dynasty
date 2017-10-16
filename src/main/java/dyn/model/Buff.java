package dyn.model;

import javax.persistence.*;

@Entity
@Table(name = "buff")
public class Buff {
    public static final String FERTILITY = "buffs.title.fertility";
    public static final String SEVEN_CHILDREN = "buffs.title.sevenChildren";
    public static final String DOMINANT_MOTHER = "buffs.title.dominantMother";
    public static final String DOMINANT_FATHER = "buffs.title.dominantFather";
    public static final String MANY_SONS = "buffs.title.manySons";
    public static final String MANY_DAUGHTERS = "buffs.title.manyDaughters";
    public static final String GENETIC_MOD = "buffs.title.geneticMod";
    public static final String ONE_MORE_CHILD = "buffs.title.oneMoreChild";
    public static final String SALARY_INC = "buffs.title.salaryInc";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    private BuffType type;

    @OneToOne
    @JoinColumn(name = "contradictory", nullable = true)
    private Buff contradictory;

    private int cost;

    @Column(name = "description")
    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BuffType getType() {
        return type;
    }

    public void setType(BuffType type) {
        this.type = type;
    }

    public Buff getContradictory() {
        return contradictory;
    }

    public void setContradictory(Buff contradictory) {
        this.contradictory = contradictory;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}