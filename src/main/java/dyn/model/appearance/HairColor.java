package dyn.model.appearance;

import javax.persistence.*;

@Entity
@Table(name = "app_hair_color")
public class HairColor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String title;

    @Enumerated(EnumType.STRING)
    private AppearanceType type;

    private String color;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AppearanceType getType() {
        return type;
    }

    public void setType(AppearanceType type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isRare() {
        return type.equals(AppearanceType.rare);
    }
}