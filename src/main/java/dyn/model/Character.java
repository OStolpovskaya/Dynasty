package dyn.model;

import dyn.model.appearance.*;
import dyn.model.career.Career;
import org.springframework.util.Base64Utils;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
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

    @ManyToOne
    @JoinColumn(name = "father", nullable = true)
    private Character father;

    @OneToOne
    @JoinColumn(name = "spouse", nullable = true)
    private Character spouse;

    private int level;

    @OneToOne
    @JoinColumn(name = "race")
    private Race race;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "career")
    private Career career;

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
    @JoinColumn(name = "hairstyle")
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
    @OneToMany(mappedBy = "father", fetch = FetchType.LAZY)
    private java.util.List<Character> children;

    @OneToOne(mappedBy = "character")
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
        if (fiancee == null) {
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

    // =============================================================================
    public void generateView() {
        try {

            // load source images
            BufferedImage bodyImageSub = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/" + sex + "/" + getBody().getName() + "_sub.png"));
            bodyImageSub = colorImage(bodyImageSub, getSkinColor().getColor());
            BufferedImage bodyImage = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/" + sex + "/" + getBody().getName() + ".png"));
            BufferedImage earsImageSub = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/" + sex + "/" + getEars().getName() + "_sub.png"));
            earsImageSub = colorImage(earsImageSub, getSkinColor().getColor());
            BufferedImage earsImage = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/" + sex + "/" + getEars().getName() + ".png"));
            BufferedImage eyebrowsImage = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/" + sex + "/" + getEyebrows().getName() + ".png"));
            BufferedImage eyesImageSub = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/" + sex + "/" + getEyes().getName() + "_sub.png"));
            BufferedImage eyeColorImage = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/" + sex + "/" + getEyeColor().getName() + ".png"));
            BufferedImage eyesImage = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/" + sex + "/" + getEyes().getName() + ".png"));
            BufferedImage hairStyleImageSub = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/" + sex + "/" + getHairStyle().getName() + "_sub.png"));
            hairStyleImageSub = colorImage(hairStyleImageSub, getHairColor().getColor());
            BufferedImage hairStyleImage = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/" + sex + "/" + getHairStyle().getName() + ".png"));
            BufferedImage headImageSub = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/" + sex + "/" + getHead().getName() + "_sub.png"));
            headImageSub = colorImage(headImageSub, getSkinColor().getColor());
            BufferedImage headImage = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/" + sex + "/" + getHead().getName() + ".png"));
            BufferedImage mouthImage = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/" + sex + "/" + getMouth().getName() + ".png"));
            BufferedImage noseImage = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/" + sex + "/" + getNose().getName() + ".png"));
            BufferedImage heightImage = ImageIO.read(ResourceUtils.getFile("classpath:static/graphics/" + sex + "/" + getHeight().getName() + ".png"));

            // create the new image, canvas size is the max. of both image sizes
            BufferedImage combined = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

            // paint images, preserving the alpha channels
            Graphics g = combined.getGraphics();
            g.drawImage(bodyImageSub, 0, 0, null);
            g.drawImage(bodyImage, 0, 0, null);
            g.drawImage(headImageSub, 0, 0, null);
            g.drawImage(headImage, 0, 0, null);
            g.drawImage(eyebrowsImage, 0, 0, null);
            g.drawImage(eyesImageSub, 0, 0, null);
            g.drawImage(eyeColorImage, 0, 0, null);
            g.drawImage(eyesImage, 0, 0, null);
            g.drawImage(mouthImage, 0, 0, null);
            g.drawImage(noseImage, 0, 0, null);
            g.drawImage(hairStyleImageSub, 0, 0, null);
            g.drawImage(hairStyleImage, 0, 0, null);
            g.drawImage(earsImageSub, 0, 0, null);
            g.drawImage(earsImage, 0, 0, null);
            g.drawImage(heightImage, 0, 0, null);

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

    private BufferedImage colorImage(BufferedImage image, String colorString) {
        int width = image.getWidth();
        int height = image.getHeight();
        WritableRaster raster = image.getRaster();

        for (int xx = 0; xx < width; xx++) {
            for (int yy = 0; yy < height; yy++) {
                int[] pixels = raster.getPixel(xx, yy, (int[]) null);
                pixels[0] = Integer.valueOf(colorString.substring(0, 2), 16);
                pixels[1] = Integer.valueOf(colorString.substring(2, 4), 16);
                pixels[2] = Integer.valueOf(colorString.substring(4, 6), 16);
                raster.setPixel(xx, yy, pixels);
            }
        }
        return image;
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
                ", body=" + body.getName() +
                ", ears=" + ears.getName() +
                ", eyebrows=" + eyebrows.getName() +
                ", eyeColor=" + eyeColor.getName() +
                ", eyes=" + eyes.getName() +
                ", hairColor=" + hairColor.getName() +
                ", hairStyle=" + hairStyle.getName() +
                ", hairType=" + hairType.getName() +
                ", head=" + head.getName() +
                ", height=" + height.getName() +
                ", mouth=" + mouth.getName() +
                ", nose=" + nose.getName() +
                ", skinColor=" + skinColor.getName() +
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
}
