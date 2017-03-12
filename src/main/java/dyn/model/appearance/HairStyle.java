package dyn.model.appearance;

import javax.persistence.*;

@Entity
@Table(name = "app_hair_style")
public class HairStyle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String sex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hair_type")
    private HairType hairType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public HairType getHairType() {
        return hairType;
    }

    public void setHairType(HairType hairType) {
        this.hairType = hairType;
    }
}