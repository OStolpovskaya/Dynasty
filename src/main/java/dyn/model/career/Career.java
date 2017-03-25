package dyn.model.career;

import dyn.model.Character;

import javax.persistence.*;

@Entity
@Table(name = "character_career")
public class Career {
    public static final int IMPROVE_COST = 350;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "vocation")
    private Vocation vocation;

    private int intelligence;
    private int charisma;
    private int strength;
    private int creativity;

    @OneToOne
    @JoinColumn(name = "education")
    private Education education;

    @OneToOne
    @JoinColumn(name = "profession")
    private Profession profession;

    // ======================================
    @OneToOne(mappedBy = "career")
    private Character character;

    // ======================================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Vocation getVocation() {
        return vocation;
    }

    public void setVocation(Vocation vocation) {
        this.vocation = vocation;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getCreativity() {
        return creativity;
    }

    public void setCreativity(int creativity) {
        this.creativity = creativity;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public int getResultSalary() {
        return getProfession().getLevel() * getVocation().getStartSalary();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Career{");
        sb.append("id=").append(id);
        sb.append(", vocation=").append(vocation.getName());
        sb.append(", intelligence=").append(intelligence);
        sb.append(", charisma=").append(charisma);
        sb.append(", strength=").append(strength);
        sb.append(", creativity=").append(creativity);
        sb.append(", education=").append(education.getName());
        sb.append(", profession=").append(profession == null ? "" : profession.getName());
        sb.append('}');
        return sb.toString();
    }
}