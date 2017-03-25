package dyn.model.career;

import javax.persistence.*;

@Entity
@Table(name = "profession")
public class Profession {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "vocation")
    private Vocation vocation;

    private String name;

    @OneToOne
    @JoinColumn(name = "education")
    private Education education;

    private int level;

    private int intelligence;
    private int charisma;
    private int strength;
    private int creativity;

    public long getId() {
        return id;
    }

    public Vocation getVocation() {
        return vocation;
    }

    public String getName() {
        return name;
    }

    public Education getEducation() {
        return education;
    }

    public int getLevel() {
        return level;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getCharisma() {
        return charisma;
    }

    public int getStrength() {
        return strength;
    }

    public int getCreativity() {
        return creativity;
    }
}