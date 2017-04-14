package dyn.model.career;

import javax.persistence.*;

@Entity
@Table(name = "education")
public class Education {
    public static final long PRIMARY = 1;
    public static final long SECONDARY = 2;
    public static final long HIGHER = 3;

    private long id;
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
}