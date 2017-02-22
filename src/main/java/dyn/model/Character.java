package dyn.model;

import dyn.model.appearance.*;

import javax.persistence.*;

/**
 * Created by OM on 21.02.2017.
 */
@Entity
@Table(name = "character")
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family")
    private Family family;

    private String name;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @OneToOne
    @JoinColumn(nullable = true)
    private Character father;

    @OneToOne
    @JoinColumn(nullable = true)
    private Character spouse;

    private int level;

    @OneToOne
    private Race race;

    // ============ APPEARANCE ============
    @OneToOne
    private Height height;

    @OneToOne
    @JoinColumn(name = "skin_color")
    private SkinColor skinColor;

    @OneToOne
    private Head head;

    @OneToOne
    private Eyes eyes;

    /*
    @OneToOne
    @JoinColumn(name="hair_color")
    private HairColor hairColor;

    @OneToOne
    @JoinColumn(name="hairstyle")
    private HairStyle hairStyle;

    @OneToOne
    private Ears ears;

    @OneToOne
    private Eyebrows eyebrows;

    @OneToOne
    @JoinColumn(name="eye_color")
    private EyeColor eyeColor;

    @OneToOne
    private Nose nose;

    @OneToOne
    private Mouth mouth;

    @OneToOne
    private Body body;
    */
    public enum Sex {
        MALE,
        FEMALE
    }
}
