package dyn.model;

import javax.persistence.*;

/**
 * Created by OM on 21.02.2017.
 */
@Entity
@Table(name = "race")
public class Race {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

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
}
