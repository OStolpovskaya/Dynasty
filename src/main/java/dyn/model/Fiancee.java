package dyn.model;

import javax.persistence.*;

@Entity
public class Fiancee {
    @Transient
    public boolean isDisabled = false;
    @Transient
    public String disableReason = "";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Character character;
    private int cost;

    @Enumerated(EnumType.STRING)
    private FianceeType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public FianceeType getType() {
        return type;
    }

    public void setType(FianceeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Fiancee{" +
                "id=" + id +
                ", character=" + character +
                ", cost=" + cost +
                '}';
    }

    public boolean isSpecialFiancee() {
        return type.equals(FianceeType.special);
    }
}