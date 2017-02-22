package dyn.model;

import javax.persistence.*;

/**
 * Created by OM on 21.02.2017.
 */
@Entity
@Table(name = "achievement")
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

}
