package dyn.model;

import dyn.model.appearance.*;
import org.springframework.util.Base64Utils;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    @OneToOne
    @JoinColumn(name = "father", nullable = true)
    private Character father;

    @OneToOne
    @JoinColumn(name = "spouse", nullable = true)
    private Character spouse;

    private int level;


    @OneToOne
    @JoinColumn(name = "race", nullable = true)
    private Race race;

    // ============ APPEARANCE ============
    @OneToOne
    @JoinColumn(name = "body")
    private Body body;

    @OneToOne
    @JoinColumn(name = "ears")
    private Ears ears;

    @OneToOne
    @JoinColumn(name = "eyebrows")
    private Eyebrows eyebrows;

    @OneToOne
    @JoinColumn(name = "eye_color")
    private EyeColor eyeColor;

    @OneToOne
    @JoinColumn(name = "eyes")
    private Eyes eyes;

    @OneToOne
    @JoinColumn(name = "hair_color")
    private HairColor hairColor;

    @OneToOne
    @JoinColumn(name = "hair_style")
    private HairStyle hairStyle;

    @OneToOne
    @JoinColumn(name = "hair_type")
    private HairType hairType;

    @OneToOne
    @JoinColumn(name = "head")
    private Head head;

    @OneToOne
    @JoinColumn(name = "height")
    private Height height;

    @OneToOne
    @JoinColumn(name = "mouth")
    private Mouth mouth;

    @OneToOne
    @JoinColumn(name = "nose")
    private Nose nose;

    @OneToOne
    @JoinColumn(name = "skin_color")
    private SkinColor skinColor;

    // ============ VIEW ============
    @Lob
    private byte[] view;

    // ============ RELATIONS ============
    @OneToMany(mappedBy = "father")
    private java.util.List<Character> children;

    @OneToOne(mappedBy = "character")
    private Fiancee fiancee;

    // ============ Buffs ============

    @ManyToMany
    @JoinTable(name = "characters_buffs",
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

    public byte[] getView() {
        return view;
    }

    public void setView(byte[] view) {
        this.view = view;
    }

    public String getEncodedView() {
        String encodeToString = Base64Utils.encodeToString(view);
        return encodeToString;
    }

    public java.util.List<Character> getChildren() {
        return children;
    }

    public Set<Buff> getBuffs() {
        return buffs;
    }

    public void generateView() {
        try {
            // load source images
            BufferedImage skinColorImage = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/female/" + getSkinColor().getName() + ".png"));
            BufferedImage headImage = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/female/" + getHead().getName() + ".png"));
            BufferedImage eyesImage = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/female/" + getEyes().getName() + ".png"));

            // create the new image, canvas size is the max. of both image sizes
            BufferedImage combined = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

            // paint both images, preserving the alpha channels
            Graphics g = combined.getGraphics();
            g.drawImage(skinColorImage, 0, 0, null);
            g.drawImage(headImage, 0, 0, null);
            g.drawImage(eyesImage, 0, 0, null);


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(combined, "png", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();

            setView(imageInByte);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isFiancee() {
        if (fiancee == null) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "Character{" +
                "id=" + id +
                ", family=" + (family == null ? "" : family.getFamilyName()) +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", father=" + (father == null ? "" : family.getFamilyName()) +
                ", spouse=" + (spouse == null ? "" : spouse.getName()) +
                ", level=" + level +
                ", race=" + (race == null ? "" : race.getName()) +
                ", height=" + (height == null ? "" : height.getName()) +
                ", skinColor=" + (skinColor == null ? "" : skinColor.getName()) +
                ", head=" + (head == null ? "" : head.getName()) +
                ", eyes=" + (eyes == null ? "" : eyes.getName()) +
                '}';
    }
}
