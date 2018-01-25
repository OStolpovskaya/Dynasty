package dyn.model.adventure;

import dyn.model.Family;

import javax.persistence.*;

@Entity
@Table(name = "family_adventures")
public class FamilyAdventure {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Family family;

    private int num;

    @OneToOne
    private Adventure adventure;

    @Enumerated(EnumType.STRING)
    private FamilyAdventureStatus status;

    private boolean current;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Adventure getAdventure() {
        return adventure;
    }

    public void setAdventure(Adventure adventure) {
        this.adventure = adventure;
    }

    public FamilyAdventureStatus getStatus() {
        return status;
    }

    public void setStatus(FamilyAdventureStatus status) {
        this.status = status;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}