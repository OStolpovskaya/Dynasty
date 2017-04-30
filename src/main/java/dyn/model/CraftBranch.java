package dyn.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by OM on 31.03.2017.
 */
@Entity
@Table(name = "craft_branch")
public class CraftBranch {
    public static final Long FURNITURE = 1L;
    public static final Long DEVICES = 2L;
    public static final Long DECOR = 3L;
    public static final Long CLOTH = 4L;
    public static final Long MEAL = 5L;
    public static final Long SERVICE_AND_BUFFS = 10L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    //=========================
    @OneToMany(mappedBy = "craftBranch")
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

    public List<Thing> getThings() {
        return things;
    }
}
