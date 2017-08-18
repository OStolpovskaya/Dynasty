package dyn.model.appearance;

import javax.persistence.*;

/**
 * Created by OM on 22.02.2017.
 */
@Entity
@Table(name = "app_eyes")
public class Eyes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String title;

    @Enumerated(EnumType.STRING)
    private AppearanceType type;

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
}
