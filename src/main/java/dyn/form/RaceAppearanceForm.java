package dyn.form;

import dyn.model.appearance.*;

import java.util.ArrayList;

/**
 * Created by OM on 13.03.2017.
 */
public class RaceAppearanceForm {
    private long raceId;
    private String title;
    private ArrayList<Body> bodyList;
    private ArrayList<Ears> earsList;
    private ArrayList<Eyebrows> eyebrowsList;
    private ArrayList<EyeColor> eyeColorList;
    private ArrayList<Eyes> eyesList;
    private ArrayList<HairColor> hairColorList;
    private ArrayList<HairType> hairTypeList;
    private ArrayList<Head> headList;
    private ArrayList<Height> heightList;
    private ArrayList<Mouth> mouthList;
    private ArrayList<Nose> noseList;
    private ArrayList<SkinColor> skinColorList;

    public RaceAppearanceForm() {
        bodyList = new ArrayList<>();
        earsList = new ArrayList<>();
        eyebrowsList = new ArrayList<>();
        eyeColorList = new ArrayList<>();
        eyesList = new ArrayList<>();
        hairColorList = new ArrayList<>();
        hairTypeList = new ArrayList<>();
        headList = new ArrayList<>();
        heightList = new ArrayList<>();
        mouthList = new ArrayList<>();
        noseList = new ArrayList<>();
        skinColorList = new ArrayList<>();
    }


    public long getRaceId() {
        return raceId;
    }

    public void setRaceId(long raceId) {
        this.raceId = raceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Body> getBodyList() {
        return bodyList;
    }

    public void setBodyList(ArrayList<Body> bodyList) {
        this.bodyList = bodyList;
    }

    public ArrayList<Ears> getEarsList() {
        return earsList;
    }

    public void setEarsList(ArrayList<Ears> earsList) {
        this.earsList = earsList;
    }

    public ArrayList<Eyebrows> getEyebrowsList() {
        return eyebrowsList;
    }

    public void setEyebrowsList(ArrayList<Eyebrows> eyebrowsList) {
        this.eyebrowsList = eyebrowsList;
    }

    public ArrayList<EyeColor> getEyeColorList() {
        return eyeColorList;
    }

    public void setEyeColorList(ArrayList<EyeColor> eyeColorList) {
        this.eyeColorList = eyeColorList;
    }

    public ArrayList<Eyes> getEyesList() {
        return eyesList;
    }

    public void setEyesList(ArrayList<Eyes> eyesList) {
        this.eyesList = eyesList;
    }

    public ArrayList<HairColor> getHairColorList() {
        return hairColorList;
    }

    public void setHairColorList(ArrayList<HairColor> hairColorList) {
        this.hairColorList = hairColorList;
    }

    public ArrayList<HairType> getHairTypeList() {
        return hairTypeList;
    }

    public void setHairTypeList(ArrayList<HairType> hairTypeList) {
        this.hairTypeList = hairTypeList;
    }

    public ArrayList<Head> getHeadList() {
        return headList;
    }

    public void setHeadList(ArrayList<Head> headList) {
        this.headList = headList;
    }

    public ArrayList<Height> getHeightList() {
        return heightList;
    }

    public void setHeightList(ArrayList<Height> heightList) {
        this.heightList = heightList;
    }

    public ArrayList<Mouth> getMouthList() {
        return mouthList;
    }

    public void setMouthList(ArrayList<Mouth> mouthList) {
        this.mouthList = mouthList;
    }

    public ArrayList<Nose> getNoseList() {
        return noseList;
    }

    public void setNoseList(ArrayList<Nose> noseList) {
        this.noseList = noseList;
    }

    public ArrayList<SkinColor> getSkinColorList() {
        return skinColorList;
    }

    public void setSkinColorList(ArrayList<SkinColor> skinColorList) {
        this.skinColorList = skinColorList;
    }

    public void fillEmptyLists() {
        if (getBodyList().isEmpty()) getBodyList().add(null);
        if (getEarsList().isEmpty()) getEarsList().add(null);
        if (getEyebrowsList().isEmpty()) getEyebrowsList().add(null);
        if (getEyeColorList().isEmpty()) getEyeColorList().add(null);
        if (getEyesList().isEmpty()) getEyesList().add(null);
        if (getHairColorList().isEmpty()) getHairColorList().add(null);
        if (getHairTypeList().isEmpty()) getHairTypeList().add(null);
        if (getHeadList().isEmpty()) getHeadList().add(null);
        if (getHeightList().isEmpty()) getHeightList().add(null);
        if (getMouthList().isEmpty()) getMouthList().add(null);
        if (getNoseList().isEmpty()) getNoseList().add(null);
        if (getSkinColorList().isEmpty()) getSkinColorList().add(null);

    }
}
