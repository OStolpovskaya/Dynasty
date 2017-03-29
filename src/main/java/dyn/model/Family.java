package dyn.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OM on 19.02.2017.
 */
@Entity
@Table(name = "family")
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Size(min = 2, max = 30, message = "{field.size2-30}")
    @Column(name = "family_name")
    private String familyName;

    private boolean current;

    @Size(min = 2, max = 30, message = "{field.size2-30}")
    @Column(name = "male_lastname")
    private String maleLastname;

    @Size(min = 2, max = 30, message = "{field.size2-30}")
    @Column(name = "female_lastname")
    private String femaleLastname;

    @Column(name = "level")
    private int level;

    @Column(name = "money")
    private int money;

    @OneToOne
    private House house;

    // ===================================
    @OneToMany(mappedBy = "family")
    private List<Character> characters;

    public Family() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public String getMaleLastname() {
        return maleLastname;
    }

    public void setMaleLastname(String maleLastname) {
        this.maleLastname = maleLastname;
    }

    public String getFemaleLastname() {
        return femaleLastname;
    }

    public void setFemaleLastname(String femaleLastname) {
        this.femaleLastname = femaleLastname;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    public List<List<Character>> getLevelOrderedFathers() {
        List<List<Character>> array = new ArrayList<>(getLevel());
        if (level > 0) {
            for (int i = 0; i < level; i++) {
                array.add(new ArrayList<>());
            }
        } else {
            array.add(new ArrayList<>());
        }

        for (Character character : characters) {
            if (character.getSex().equals("male") && character.getSpouse() != null) {
                array.get(character.getLevel()).add(character);
            }
        }
        return array;
    }

    @Override
    public String toString() {
        return "Family{" +
                "id=" + id +
                ", user=" + (user == null ? "" : user.getUserName()) +
                ", familyName='" + familyName + '\'' +
                ", current=" + current +
                ", maleLastname='" + maleLastname + '\'' +
                ", femaleLastname='" + femaleLastname + '\'' +
                ", level=" + level +
                '}';
    }
}
