package dyn.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by OM on 31.03.2017.
 */
@Entity
@Table(name = "craft_branch")
public class CraftBranch {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private int point;

    //=========================
    @OneToMany(mappedBy = "craftBranch")
    @OrderBy("craftNumber ASC")
    private List<Thing> things;
    //=========================

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

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public List<Thing> getThings() {
        return things;
    }
}
