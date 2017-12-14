package dyn.model.appearance;

import javax.persistence.*;

@Entity
@Table(name = "app_eye_color")
public class EyeColor implements Appearance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String title;

    @Enumerated(EnumType.STRING)
    private AppearanceType type;

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

    public boolean isRare() {
        return type.equals(AppearanceType.rare);
    }

    public String getNamePart() {
        return name.substring(name.lastIndexOf(".") + 1);
    }
}