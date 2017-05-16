package dyn.model;

import javax.persistence.*;

@Entity
@Table(name = "family_building")
public class FamilyBuilding {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private House building;

    @ManyToOne
    @JoinColumn(name = "family_id")
    private Family family;

    @Column(name = "building_quality")
    private float buildingQuality;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public House getBuilding() {
        return building;
    }

    public void setBuilding(House building) {
        this.building = building;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public float getBuildingQuality() {
        return buildingQuality;
    }

    public void setBuildingQuality(float buildingQuality) {
        this.buildingQuality = buildingQuality;
    }
}