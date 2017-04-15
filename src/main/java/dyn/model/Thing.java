package dyn.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by OM on 21.02.2017.
 */
@Entity
@Table(name = "thing")
public class Thing {
    // =================================================
    @Transient
    public int familyStatus;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "craft_branch_id")
    private CraftBranch craftBranch;
    private int craftNumber;
    private int width;
    private int height;
    // =================================================
    @OneToMany(mappedBy = "thing")
    private List<Project> projects;

    @ManyToOne
    @JoinColumn(name = "parent", nullable = true)
    private Thing parent;
    // =================================================

    @OneToMany(mappedBy = "parent")
    private List<Thing> childThings;

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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public CraftBranch getCraftBranch() {
        return craftBranch;
    }

    public void setCraftBranch(CraftBranch craftBranch) {
        this.craftBranch = craftBranch;
    }

    public int getCraftNumber() {
        return craftNumber;
    }

    public void setCraftNumber(int craftNumber) {
        this.craftNumber = craftNumber;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public List<Thing> getChildThings() {
        return childThings;
    }

    public Thing getParent() {
        return parent;
    }
}
