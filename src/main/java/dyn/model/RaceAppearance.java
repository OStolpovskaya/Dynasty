package dyn.model;

import dyn.model.appearance.*;

import javax.persistence.*;

/**
 * Created by OM on 21.02.2017.
 */
@Entity
@Table(name = "race_appearance")
public class RaceAppearance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "race_id")
    private Race race;

    // ============ APPEARANCE ============
    @OneToOne
    @JoinColumn(name = "body", nullable = true)
    private Body body;

    @OneToOne
    @JoinColumn(name = "ears", nullable = true)
    private Ears ears;

    @OneToOne
    @JoinColumn(name = "eyebrows", nullable = true)
    private Eyebrows eyebrows;

    @OneToOne
    @JoinColumn(name = "eye_color", nullable = true)
    private EyeColor eyeColor;

    @OneToOne
    @JoinColumn(name = "eyes", nullable = true)
    private Eyes eyes;

    @OneToOne
    @JoinColumn(name = "hair_color", nullable = true)
    private HairColor hairColor;

    @OneToOne
    @JoinColumn(name = "hair_type", nullable = true)
    private HairType hairType;

    @OneToOne
    @JoinColumn(name = "head", nullable = true)
    private Head head;

    @OneToOne
    @JoinColumn(name = "height", nullable = true)
    private Height height;

    @OneToOne
    @JoinColumn(name = "mouth", nullable = true)
    private Mouth mouth;

    @OneToOne
    @JoinColumn(name = "nose", nullable = true)
    private Nose nose;

    @OneToOne
    @JoinColumn(name = "skin_color", nullable = true)
    private SkinColor skinColor;

    // ===============================
    public RaceAppearance() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Ears getEars() {
        return ears;
    }

    public void setEars(Ears ears) {
        this.ears = ears;
    }

    public Eyebrows getEyebrows() {
        return eyebrows;
    }

    public void setEyebrows(Eyebrows eyebrows) {
        this.eyebrows = eyebrows;
    }

    public EyeColor getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(EyeColor eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Eyes getEyes() {
        return eyes;
    }

    public void setEyes(Eyes eyes) {
        this.eyes = eyes;
    }

    public HairColor getHairColor() {
        return hairColor;
    }

    public void setHairColor(HairColor hairColor) {
        this.hairColor = hairColor;
    }

    public HairType getHairType() {
        return hairType;
    }

    public void setHairType(HairType hairType) {
        this.hairType = hairType;
    }

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public Height getHeight() {
        return height;
    }

    public void setHeight(Height height) {
        this.height = height;
    }

    public Mouth getMouth() {
        return mouth;
    }

    public void setMouth(Mouth mouth) {
        this.mouth = mouth;
    }

    public Nose getNose() {
        return nose;
    }

    public void setNose(Nose nose) {
        this.nose = nose;
    }

    public SkinColor getSkinColor() {
        return skinColor;
    }

    public void setSkinColor(SkinColor skinColor) {
        this.skinColor = skinColor;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RaceAppearance{");
        sb.append("id=").append(id);
        sb.append(", race=").append(race);
        sb.append(", body=").append(body == null ? "null" : body.getName());
        sb.append(", ears=").append(ears == null ? "null" : ears.getName());
        sb.append(", eyebrows=").append(eyebrows == null ? "null" : eyebrows.getName());
        sb.append(", eyeColor=").append(eyeColor == null ? "null" : eyeColor.getName());
        sb.append(", eyes=").append(eyes == null ? "null" : eyes.getName());
        sb.append(", hairColor=").append(hairColor == null ? "null" : hairColor.getName());
        sb.append(", hairType=").append(hairType == null ? "null" : hairType.getName());
        sb.append(", head=").append(head == null ? "null" : head.getName());
        sb.append(", height=").append(height == null ? "null" : height.getName());
        sb.append(", mouth=").append(mouth == null ? "null" : mouth.getName());
        sb.append(", nose=").append(nose == null ? "null" : nose.getName());
        sb.append(", skinColor=").append(skinColor == null ? "null" : skinColor.getName());
        sb.append('}');
        return sb.toString();
    }
}
