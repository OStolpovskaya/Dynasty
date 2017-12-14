package dyn.model;

import dyn.model.appearance.*;
import dyn.model.career.Career;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by OM on 21.02.2017.
 */
@Entity
@Table(name = "characters")
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family")
    private Family family;

    private String name;

    private String sex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "father", nullable = true)
    private Character father;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spouse", nullable = true)
    private Character spouse;

    private int level;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "race")
    private Race race;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "career")
    private Career career;

    // ============ APPEARANCE ============
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "body")
    private Body body;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ears")
    private Ears ears;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "eyebrows")
    private Eyebrows eyebrows;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "eye_color")
    private EyeColor eyeColor;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "eyes")
    private Eyes eyes;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hair_color")
    private HairColor hairColor;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hairstyle")
    private HairStyle hairStyle;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hair_type")
    private HairType hairType;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "head")
    private Head head;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "height")
    private Height height;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mouth")
    private Mouth mouth;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nose")
    private Nose nose;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skin_color")
    private SkinColor skinColor;

    // ============ RELATIONS ============
    @OneToMany(mappedBy = "father", fetch = FetchType.LAZY)
    private java.util.List<Character> children;

    @OneToOne(mappedBy = "character", fetch = FetchType.EAGER)
    private Fiancee fiancee;

    // ============ Buffs ============

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "character_buffs",
            joinColumns = {@JoinColumn(name = "character_id")},
            inverseJoinColumns = {@JoinColumn(name = "buff_id")})
    private Set<Buff> buffs = new HashSet<Buff>();

    // ===============================
    public Character() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Character getFather() {
        return father;
    }

    public void setFather(Character father) {
        this.father = father;
    }

    public Character getSpouse() {
        return spouse;
    }

    public void setSpouse(Character spouse) {
        this.spouse = spouse;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Height getHeight() {
        return height;
    }

    public void setHeight(Height height) {
        this.height = height;
    }

    public SkinColor getSkinColor() {
        return skinColor;
    }

    public void setSkinColor(SkinColor skinColor) {
        this.skinColor = skinColor;
    }

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public Eyes getEyes() {
        return eyes;
    }

    public void setEyes(Eyes eyes) {
        this.eyes = eyes;
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

    public HairColor getHairColor() {
        return hairColor;
    }

    public void setHairColor(HairColor hairColor) {
        this.hairColor = hairColor;
    }

    public HairStyle getHairStyle() {
        return hairStyle;
    }

    public void setHairStyle(HairStyle hairStyle) {
        this.hairStyle = hairStyle;
    }

    public HairType getHairType() {
        return hairType;
    }

    public void setHairType(HairType hairType) {
        this.hairType = hairType;
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

    public java.util.List<Character> getChildren() {
        return children;
    }

    public Set<Buff> getBuffs() {
        return buffs;
    }

    public boolean isBuffedBy(String buffName) {
        for (Buff buff : buffs) {
            if (buff.getTitle().equals(buffName)) {
                return true;
            }
        }
        return false;
    }

    public Career getCareer() {
        return career;
    }

    public void setCareer(Career career) {
        this.career = career;
    }

    public Fiancee getFiancee() {
        return fiancee;
    }

    public boolean isFiancee() {
        if (sex.equals("male") || fiancee == null) {
            return false;
        }
        return true;
    }

    public boolean hasRareFeatures() {

        boolean result = body.isRare() ||
                ears.isRare() ||
                eyebrows.isRare() ||
                eyeColor.isRare() ||
                eyes.isRare() ||
                hairColor.isRare() ||
                hairType.isRare() ||
                head.isRare() ||
                height.isRare() ||
                mouth.isRare() ||
                nose.isRare() ||
                skinColor.isRare();
        return result;
    }

    public String getFullName() {
        String lastname;
        if (family == null) {
            lastname = "";
        } else {
            if (sex.equals("male")) {
                lastname = family.getMaleLastname();
            } else {
                lastname = family.getFemaleLastname();
            }
        }
        return lastname + " " + name;
    }

    @Override
    public String toString() {
        return "Character{" +
                "id=" + id +
                ", family=" + (family != null ? family.getFamilyName() : "") +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", father=" + (father != null ? father.getName() : "") +
                ", spouse=" + (spouse != null ? spouse.getName() : "") +
                ", level=" + level +
                ", race=" + race.getName() +
                ", body=" + body.getTitle() +
                ", ears=" + ears.getTitle() +
                ", eyebrows=" + eyebrows.getTitle() +
                ", eyeColor=" + eyeColor.getTitle() +
                ", eyes=" + eyes.getTitle() +
                ", hairColor=" + hairColor.getTitle() +
                ", hairStyle=" + hairStyle.getTitle() +
                ", hairType=" + hairType.getTitle() +
                ", head=" + head.getTitle() +
                ", height=" + height.getTitle() +
                ", mouth=" + mouth.getTitle() +
                ", nose=" + nose.getTitle() +
                ", skinColor=" + skinColor.getTitle() +
                ", children amount =" + (children != null ? children.size() : "-") +
                ", fiancee=" + isFiancee() +
                ", buffs=" + (buffs != null ? buffs.size() : "-") +
                '}';
    }

    public boolean hasSpouse() {
        return (spouse != null);
    }

    public String getMainDetails() {
        return "Character{" +
                "id=" + id +
                ", family=" + (family != null ? family.getFamilyName() : "") +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", sex='" + sex + '\'' +
                ", father=" + (father != null ? father.getName() : "") +
                ", spouse=" + (spouse != null ? spouse.getName() : "") +
                ", fiancee=" + isFiancee() +
                ", race=" + race.getName() +
                '}';
    }

    public int getRaceCoefficient() {
        if (race.getId() == 1L) {
            return 1;
        }
        if (race.getId() == 2L) {
            return 2;
        }
        if (2L < race.getId() && race.getId() < 6L) {
            return 3;
        }
        if (race.getId() > 5L) {
            return 4;
        }

        return 1;
    }
}
