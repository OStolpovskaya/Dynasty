package dyn.model.adventure;

import dyn.model.*;
import dyn.model.appearance.*;
import dyn.model.career.Vocation;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "adventure")
public class Adventure {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    private AdventureStatus status;

    @Size(min = 6, max = 100, message = "{adventure.creation.titleSize}")
    private String title;

    @OneToOne
    private Landscape landscape;
    @OneToOne
    private Subject subject;

    @Size(min = 50, max = 800, message = "{adventure.creation.textDescSize}")
    private String textDesc;

    @Size(min = 50, max = 600, message = "{adventure.creation.textSuccessSize}")
    private String textSuccess;

    @Size(min = 50, max = 400, message = "{adventure.creation.textFailedSize}")
    private String textFailed;

    @Enumerated(EnumType.STRING)
    private AnswerType answerType;

    private Date creationDate;

    @ManyToOne
    private Family createdBy;

    // === Items ===

    @OneToOne
    private Thing thing1;
    @OneToOne
    private Thing thing2;
    @OneToOne
    private Thing thing3;

    // === Character ===

    private String charSex;

    @Enumerated(EnumType.STRING)
    private CharSkill charIntelligence;
    @Enumerated(EnumType.STRING)
    private CharSkill charCharisma;
    @Enumerated(EnumType.STRING)
    private CharSkill charStrength;
    @Enumerated(EnumType.STRING)
    private CharSkill charCreativity;

    @OneToOne
    private Race charRace;
    @OneToOne
    private Vocation charVocation;

    @OneToOne
    private Body charAppBody;
    @OneToOne
    private Ears charAppEars;
    @OneToOne
    private Eyebrows charAppEyebrows;
    @OneToOne
    private Eyes charAppEyes;
    @OneToOne
    private EyeColor charAppEyeColor;
    @OneToOne
    private HairColor charAppHairColor;
    @OneToOne
    private HairType charAppHairType;
    @OneToOne
    private Head charAppHead;
    @OneToOne
    private Height charAppHeight;
    @OneToOne
    private Mouth charAppMouth;
    @OneToOne
    private Nose charAppNose;
    @OneToOne
    private SkinColor charAppSkinColor;

    // === actions with character and items

    @Enumerated(EnumType.STRING)
    private ActionThing actionThing1;
    @Enumerated(EnumType.STRING)
    private ActionThing actionThing2;
    @Enumerated(EnumType.STRING)
    private ActionThing actionThing3;

    @Enumerated(EnumType.STRING)
    private ActionChar actionChar;

    @Enumerated(EnumType.STRING)
    private ActionCharSkill actionCharIntelligence;
    @Enumerated(EnumType.STRING)
    private ActionCharSkill actionCharCharisma;
    @Enumerated(EnumType.STRING)
    private ActionCharSkill actionCharStrength;
    @Enumerated(EnumType.STRING)
    private ActionCharSkill actionCharCreativity;

    @OneToOne
    private Race actionCharRace;
    @OneToOne
    private Vocation actionCharVocation;

    @OneToOne
    private Body actionCharAppBody;
    @OneToOne
    private Ears actionCharAppEars;
    @OneToOne
    private Eyebrows actionCharAppEyebrows;
    @OneToOne
    private Eyes actionCharAppEyes;
    @OneToOne
    private EyeColor actionCharAppEyeColor;
    @OneToOne
    private HairColor actionCharAppHairColor;
    @OneToOne
    private HairType actionCharAppHairType;
    @OneToOne
    private Head actionCharAppHead;
    @OneToOne
    private Height actionCharAppHeight;
    @OneToOne
    private Mouth actionCharAppMouth;
    @OneToOne
    private Nose actionCharAppNose;
    @OneToOne
    private SkinColor actionCharAppSkinColor;

    // === Award ===
    @Enumerated(EnumType.STRING)
    private ResType awardResType;
    private int awardResAmount;
    private int awardMoney;
    private int awardCraftPoint;
    @OneToOne
    private Thing awardThing;
    @OneToOne
    private Project awardBuff;

    // === Statistics ===
    private int proposedTimes;
    private int completedTimes;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AdventureStatus getStatus() {
        return status;
    }

    public void setStatus(AdventureStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Landscape getLandscape() {
        return landscape;
    }

    public void setLandscape(Landscape landscape) {
        this.landscape = landscape;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getTextDesc() {
        return textDesc;
    }

    public void setTextDesc(String textDesc) {
        this.textDesc = textDesc;
    }

    public String getTextSuccess() {
        return textSuccess;
    }

    public void setTextSuccess(String textSuccess) {
        this.textSuccess = textSuccess;
    }

    public String getTextFailed() {
        return textFailed;
    }

    public void setTextFailed(String textFailed) {
        this.textFailed = textFailed;
    }

    public AnswerType getAnswerType() {
        return answerType;
    }

    public void setAnswerType(AnswerType answerType) {
        this.answerType = answerType;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Family getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Family createdBy) {
        this.createdBy = createdBy;
    }

    public Thing getThing1() {
        return thing1;
    }

    public void setThing1(Thing thing1) {
        this.thing1 = thing1;
    }

    public Thing getThing2() {
        return thing2;
    }

    public void setThing2(Thing thing2) {
        this.thing2 = thing2;
    }

    public Thing getThing3() {
        return thing3;
    }

    public void setThing3(Thing thing3) {
        this.thing3 = thing3;
    }

    public String getCharSex() {
        return charSex;
    }

    public void setCharSex(String charSex) {
        this.charSex = charSex;
    }

    public CharSkill getCharIntelligence() {
        return charIntelligence;
    }

    public void setCharIntelligence(CharSkill charIntelligence) {
        this.charIntelligence = charIntelligence;
    }

    public CharSkill getCharCharisma() {
        return charCharisma;
    }

    public void setCharCharisma(CharSkill charCharisma) {
        this.charCharisma = charCharisma;
    }

    public CharSkill getCharStrength() {
        return charStrength;
    }

    public void setCharStrength(CharSkill charStrength) {
        this.charStrength = charStrength;
    }

    public CharSkill getCharCreativity() {
        return charCreativity;
    }

    public void setCharCreativity(CharSkill charCreativity) {
        this.charCreativity = charCreativity;
    }

    public Race getCharRace() {
        return charRace;
    }

    public void setCharRace(Race charRace) {
        this.charRace = charRace;
    }

    public Vocation getCharVocation() {
        return charVocation;
    }

    public void setCharVocation(Vocation charVocation) {
        this.charVocation = charVocation;
    }

    public Body getCharAppBody() {
        return charAppBody;
    }

    public void setCharAppBody(Body charAppBody) {
        this.charAppBody = charAppBody;
    }

    public Ears getCharAppEars() {
        return charAppEars;
    }

    public void setCharAppEars(Ears charAppEars) {
        this.charAppEars = charAppEars;
    }

    public Eyebrows getCharAppEyebrows() {
        return charAppEyebrows;
    }

    public void setCharAppEyebrows(Eyebrows charAppEyebrows) {
        this.charAppEyebrows = charAppEyebrows;
    }

    public Eyes getCharAppEyes() {
        return charAppEyes;
    }

    public void setCharAppEyes(Eyes charAppEyes) {
        this.charAppEyes = charAppEyes;
    }

    public EyeColor getCharAppEyeColor() {
        return charAppEyeColor;
    }

    public void setCharAppEyeColor(EyeColor charAppEyeColor) {
        this.charAppEyeColor = charAppEyeColor;
    }

    public HairColor getCharAppHairColor() {
        return charAppHairColor;
    }

    public void setCharAppHairColor(HairColor charAppHairColor) {
        this.charAppHairColor = charAppHairColor;
    }

    public HairType getCharAppHairType() {
        return charAppHairType;
    }

    public void setCharAppHairType(HairType charAppHairType) {
        this.charAppHairType = charAppHairType;
    }

    public Head getCharAppHead() {
        return charAppHead;
    }

    public void setCharAppHead(Head charAppHead) {
        this.charAppHead = charAppHead;
    }

    public Height getCharAppHeight() {
        return charAppHeight;
    }

    public void setCharAppHeight(Height charAppHeight) {
        this.charAppHeight = charAppHeight;
    }

    public Mouth getCharAppMouth() {
        return charAppMouth;
    }

    public void setCharAppMouth(Mouth charAppMouth) {
        this.charAppMouth = charAppMouth;
    }

    public Nose getCharAppNose() {
        return charAppNose;
    }

    public void setCharAppNose(Nose charAppNose) {
        this.charAppNose = charAppNose;
    }

    public SkinColor getCharAppSkinColor() {
        return charAppSkinColor;
    }

    public void setCharAppSkinColor(SkinColor charAppSkinColor) {
        this.charAppSkinColor = charAppSkinColor;
    }

    public ActionThing getActionThing1() {
        return actionThing1;
    }

    public void setActionThing1(ActionThing actionThing1) {
        this.actionThing1 = actionThing1;
    }

    public ActionThing getActionThing2() {
        return actionThing2;
    }

    public void setActionThing2(ActionThing actionThing2) {
        this.actionThing2 = actionThing2;
    }

    public ActionThing getActionThing3() {
        return actionThing3;
    }

    public void setActionThing3(ActionThing actionThing3) {
        this.actionThing3 = actionThing3;
    }

    public ActionChar getActionChar() {
        return actionChar;
    }

    public void setActionChar(ActionChar actionChar) {
        this.actionChar = actionChar;
    }

    public ActionCharSkill getActionCharIntelligence() {
        return actionCharIntelligence;
    }

    public void setActionCharIntelligence(ActionCharSkill actionCharIntelligence) {
        this.actionCharIntelligence = actionCharIntelligence;
    }

    public ActionCharSkill getActionCharCharisma() {
        return actionCharCharisma;
    }

    public void setActionCharCharisma(ActionCharSkill actionCharCharisma) {
        this.actionCharCharisma = actionCharCharisma;
    }

    public ActionCharSkill getActionCharStrength() {
        return actionCharStrength;
    }

    public void setActionCharStrength(ActionCharSkill actionCharStrength) {
        this.actionCharStrength = actionCharStrength;
    }

    public ActionCharSkill getActionCharCreativity() {
        return actionCharCreativity;
    }

    public void setActionCharCreativity(ActionCharSkill actionCharCreativity) {
        this.actionCharCreativity = actionCharCreativity;
    }

    public Race getActionCharRace() {
        return actionCharRace;
    }

    public void setActionCharRace(Race actionCharRace) {
        this.actionCharRace = actionCharRace;
    }

    public Vocation getActionCharVocation() {
        return actionCharVocation;
    }

    public void setActionCharVocation(Vocation actionCharVocation) {
        this.actionCharVocation = actionCharVocation;
    }

    public Body getActionCharAppBody() {
        return actionCharAppBody;
    }

    public void setActionCharAppBody(Body actionCharAppBody) {
        this.actionCharAppBody = actionCharAppBody;
    }

    public Ears getActionCharAppEars() {
        return actionCharAppEars;
    }

    public void setActionCharAppEars(Ears actionCharAppEars) {
        this.actionCharAppEars = actionCharAppEars;
    }

    public Eyebrows getActionCharAppEyebrows() {
        return actionCharAppEyebrows;
    }

    public void setActionCharAppEyebrows(Eyebrows actionCharAppEyebrows) {
        this.actionCharAppEyebrows = actionCharAppEyebrows;
    }

    public Eyes getActionCharAppEyes() {
        return actionCharAppEyes;
    }

    public void setActionCharAppEyes(Eyes actionCharAppEyes) {
        this.actionCharAppEyes = actionCharAppEyes;
    }

    public EyeColor getActionCharAppEyeColor() {
        return actionCharAppEyeColor;
    }

    public void setActionCharAppEyeColor(EyeColor actionCharAppEyeColor) {
        this.actionCharAppEyeColor = actionCharAppEyeColor;
    }

    public HairColor getActionCharAppHairColor() {
        return actionCharAppHairColor;
    }

    public void setActionCharAppHairColor(HairColor actionCharAppHairColor) {
        this.actionCharAppHairColor = actionCharAppHairColor;
    }

    public HairType getActionCharAppHairType() {
        return actionCharAppHairType;
    }

    public void setActionCharAppHairType(HairType actionCharAppHairType) {
        this.actionCharAppHairType = actionCharAppHairType;
    }

    public Head getActionCharAppHead() {
        return actionCharAppHead;
    }

    public void setActionCharAppHead(Head actionCharAppHead) {
        this.actionCharAppHead = actionCharAppHead;
    }

    public Height getActionCharAppHeight() {
        return actionCharAppHeight;
    }

    public void setActionCharAppHeight(Height actionCharAppHeight) {
        this.actionCharAppHeight = actionCharAppHeight;
    }

    public Mouth getActionCharAppMouth() {
        return actionCharAppMouth;
    }

    public void setActionCharAppMouth(Mouth actionCharAppMouth) {
        this.actionCharAppMouth = actionCharAppMouth;
    }

    public Nose getActionCharAppNose() {
        return actionCharAppNose;
    }

    public void setActionCharAppNose(Nose actionCharAppNose) {
        this.actionCharAppNose = actionCharAppNose;
    }

    public SkinColor getActionCharAppSkinColor() {
        return actionCharAppSkinColor;
    }

    public void setActionCharAppSkinColor(SkinColor actionCharAppSkinColor) {
        this.actionCharAppSkinColor = actionCharAppSkinColor;
    }

    public ResType getAwardResType() {
        return awardResType;
    }

    public void setAwardResType(ResType awardResType) {
        this.awardResType = awardResType;
    }

    public int getAwardResAmount() {
        return awardResAmount;
    }

    public void setAwardResAmount(int awardResAmount) {
        this.awardResAmount = awardResAmount;
    }

    public int getAwardMoney() {
        return awardMoney;
    }

    public void setAwardMoney(int awardMoney) {
        this.awardMoney = awardMoney;
    }

    public int getAwardCraftPoint() {
        return awardCraftPoint;
    }

    public void setAwardCraftPoint(int awardCraftPoint) {
        this.awardCraftPoint = awardCraftPoint;
    }

    public Thing getAwardThing() {
        return awardThing;
    }

    public void setAwardThing(Thing awardThing) {
        this.awardThing = awardThing;
    }

    public Project getAwardBuff() {
        return awardBuff;
    }

    public void setAwardBuff(Project awardBuff) {
        this.awardBuff = awardBuff;
    }

    public int getProposedTimes() {
        return proposedTimes;
    }

    public void setProposedTimes(int proposedTimes) {
        this.proposedTimes = proposedTimes;
    }

    public int getCompletedTimes() {
        return completedTimes;
    }

    public void setCompletedTimes(int completedTimes) {
        this.completedTimes = completedTimes;
    }

    public String fullTitle() {
        return title + " от " + createdBy.link();
    }

    public void incProposedTimes() {
        proposedTimes++;
    }

    public void incCompletedTimes() {
        completedTimes++;
    }
}