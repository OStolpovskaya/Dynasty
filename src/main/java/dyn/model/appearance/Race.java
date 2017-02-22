package dyn.model.appearance;

import dyn.model.Achievement;

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

    @OneToOne
    private Achievement achievement;
}
