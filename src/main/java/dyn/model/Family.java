package dyn.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by OM on 19.02.2017.
 */
@Entity
@Table(name = "family")
public class Family {

    private Long id;
    private User user;

    private String familyName;
    private String maleLastname;
    private String femaleLastname;

    private boolean current;
    private int level;
    private int money;
    private int craftPoint;

    private House house;

    private int pairsNum;
    private int fianceeNum;

    // ===================================
    private List<Character> characters;
    private FamilyResources familyResources;

    private Set<Thing> craftThings = new HashSet<>();
    private Set<Project> craftProjects = new HashSet<>();
    private List<Item> items;

    private List<FamilyLog> familyLogs;
    // ===================================

    public Family() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    @Size(min = 2, max = 30, message = "{field.size2-30}")
    @Column(name = "family_name")
    public String getFamilyName() {
        return familyName;
    }
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    @Size(min = 2, max = 30, message = "{field.size2-30}")
    @Column(name = "male_lastname")
    public String getMaleLastname() {
        return maleLastname;
    }
    public void setMaleLastname(String maleLastname) {
        this.maleLastname = maleLastname;
    }

    @Size(min = 2, max = 30, message = "{field.size2-30}")
    @Column(name = "female_lastname")
    public String getFemaleLastname() {
        return femaleLastname;
    }
    public void setFemaleLastname(String femaleLastname) {
        this.femaleLastname = femaleLastname;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
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

    public int getCraftPoint() {
        return craftPoint;
    }
    public void setCraftPoint(int craftPoint) {
        this.craftPoint = craftPoint;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "family_craft_thing",
            joinColumns = {@JoinColumn(name = "family_id")},
            inverseJoinColumns = {@JoinColumn(name = "thing_id")})
    public Set<Thing> getCraftThings() {
        return craftThings;
    }
    public void setCraftThings(Set<Thing> craftThings) {
        this.craftThings = craftThings;
    }


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "family_craft_project",
            joinColumns = {@JoinColumn(name = "family_id")},
            inverseJoinColumns = {@JoinColumn(name = "project_id")})
    @OrderBy("name")
    public Set<Project> getCraftProjects() {
        return craftProjects;
    }
    public void setCraftProjects(Set<Project> craftProjects) {
        this.craftProjects = craftProjects;
    }

    @OneToOne(fetch = FetchType.LAZY)
    public House getHouse() {
        return house;
    }
    public void setHouse(House house) {
        this.house = house;
    }

    public int getPairsNum() {
        return pairsNum;
    }

    public void setPairsNum(int pairs_num) {
        this.pairsNum = pairs_num;
    }

    public int getFianceeNum() {
        return fianceeNum;
    }

    public void setFianceeNum(int fiancee_num) {
        this.fianceeNum = fiancee_num;
    }

    @OneToMany(mappedBy = "family", fetch = FetchType.LAZY)
    public List<Character> getCharacters() {
        return characters;
    }
    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "resources")
    public FamilyResources getFamilyResources() {
        return familyResources;
    }
    public void setFamilyResources(FamilyResources familyResources) {
        this.familyResources = familyResources;
    }

    public List<Project> getCraftProjectsForThing(Thing thing) {
        List<Project> craftProjectsForThing = new ArrayList<>();
        for (Project craftProject : craftProjects) {
            if (craftProject.getThing() == thing) {
                craftProjectsForThing.add(craftProject);
            }
        }
        return craftProjectsForThing;
    }

    @OneToMany(mappedBy = "family", fetch = FetchType.LAZY)
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
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

    public boolean hasResourcesForProject(Project project) {
        return familyResources.getWood() >= project.getWood() &&
                familyResources.getMetall() >= project.getMetall() &&
                familyResources.getPlastic() >= project.getPlastic() &&
                familyResources.getMicroelectronics() >= project.getMicroelectronics() &&
                familyResources.getCloth() >= project.getCloth() &&
                familyResources.getStone() >= project.getStone() &&
                familyResources.getChemical() >= project.getChemical();
    }

    public String logName() {
        return "Family " + familyName + "(" + id + ") ";
    }

    @OneToMany(mappedBy = "family", fetch = FetchType.LAZY)
    public List<FamilyLog> getFamilyLogs() {
        return familyLogs;
    }

    public void setFamilyLogs(List<FamilyLog> familyLogs) {
        this.familyLogs = familyLogs;
    }
}
