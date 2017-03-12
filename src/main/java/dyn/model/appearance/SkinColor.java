package dyn.model.appearance;

import javax.persistence.*;

/**
 * Created by OM on 21.02.2017.
 */
@Entity
@Table(name = "app_skin_color")
public class SkinColor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private AppearanceType type;

    private String color;

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
}
