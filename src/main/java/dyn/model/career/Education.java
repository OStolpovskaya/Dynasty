package dyn.model.career;

import javax.persistence.*;

@Entity
@Table(name = "education")
public class Education {
    public static final String PRIMARY = "edu.primary";
    public static final String SECONDARY = "edu.secondary";
    public static final String HIGHER = "edu.higher";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

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