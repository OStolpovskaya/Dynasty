package dyn.service;

import dyn.form.RaceAppearanceForm;
import dyn.model.appearance.*;
import dyn.repository.appearance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OM on 11.03.2017.
 */
@Service
public class AppearanceService {
    public static final String ALL = "all";
    public static final String USUAL = "usual";
    public static final String RARE = "rare";
    @Autowired
    BodyRepository bodyRepository;
    @Autowired
    EarsRepository earsRepository;
    @Autowired
    EyebrowsRepository eyebrowsRepository;
    @Autowired
    EyeColorRepository eyeColorRepository;
    @Autowired
    EyesRepository eyesRepository;
    @Autowired
    HairColorRepository hairColorRepository;
    @Autowired
    HairStyleRepository hairStyleRepository;
    @Autowired
    HairTypeRepository hairTypeRepository;
    @Autowired
    HeadRepository headRepository;
    @Autowired
    HeightRepository heightRepository;
    @Autowired
    MouthRepository mouthRepository;
    @Autowired
    NoseRepository noseRepository;
    @Autowired
    SkinColorRepository skinColorRepository;

    public Body getRandomBody(String type) {
        switch (type) {
            case USUAL:
                return bodyRepository.getRandomUsual();
            case RARE:
                return bodyRepository.getRandomRare();
            default:
                return bodyRepository.getRandom();
        }
    }

    public Ears getRandomEars(String type) {
        switch (type) {
            case USUAL:
                return earsRepository.getRandomUsual();
            case RARE:
                return earsRepository.getRandomRare();
            default:
                return earsRepository.getRandom();
        }
    }

    public Eyebrows getRandomEyeBrows(String type) {
        switch (type) {
            case USUAL:
                return eyebrowsRepository.getRandomUsual();
            case RARE:
                return eyebrowsRepository.getRandomRare();
            default:
                return eyebrowsRepository.getRandom();
        }
    }

    public EyeColor getRandomEyeColor(String type) {
        switch (type) {
            case USUAL:
                return eyeColorRepository.getRandomUsual();
            case RARE:
                return eyeColorRepository.getRandomRare();
            default:
                return eyeColorRepository.getRandom();
        }
    }

    public Eyes getRandomEyes(String type) {
        switch (type) {
            case USUAL:
                return eyesRepository.getRandomUsual();
            case RARE:
                return eyesRepository.getRandomRare();
            default:
                return eyesRepository.getRandom();
        }
    }

    public HairColor getRandomHairColor(String type) {
        switch (type) {
            case USUAL:
                return hairColorRepository.getRandomUsual();
            case RARE:
                return hairColorRepository.getRandomRare();
            default:
                return hairColorRepository.getRandom();
        }
    }

    public HairStyle getRandomHairStyle(String sex, HairType hairType) {
        return hairStyleRepository.getRandom(sex, hairType.getId());
    }

    public HairType getRandomHairType(String type) {
        switch (type) {
            case USUAL:
                return hairTypeRepository.getRandomUsual();
            case RARE:
                return hairTypeRepository.getRandomRare();
            default:
                return hairTypeRepository.getRandom();
        }
    }

    public Head getRandomHead(String type) {
        switch (type) {
            case USUAL:
                return headRepository.getRandomUsual();
            case RARE:
                return headRepository.getRandomRare();
            default:
                return headRepository.getRandom();
        }
    }

    public Height getRandomHeight(String type) {
        switch (type) {
            case USUAL:
                return heightRepository.getRandomUsual();
            case RARE:
                return heightRepository.getRandomRare();
            default:
                return heightRepository.getRandom();
        }
    }

    public Mouth getRandomMouth(String type) {
        switch (type) {
            case USUAL:
                return mouthRepository.getRandomUsual();
            case RARE:
                return mouthRepository.getRandomRare();
            default:
                return mouthRepository.getRandom();
        }
    }

    public Nose getRandomNose(String type) {
        switch (type) {
            case USUAL:
                return noseRepository.getRandomUsual();
            case RARE:
                return noseRepository.getRandomRare();
            default:
                return noseRepository.getRandom();
        }
    }

    public SkinColor getRandomSkinColor(String type) {
        switch (type) {
            case USUAL:
                return skinColorRepository.getRandomUsual();
            case RARE:
                return skinColorRepository.getRandomRare();
            default:
                return skinColorRepository.getRandom();
        }
    }

    // ====================== LIST =====================================
    public List<Body> getBodyList(String type) {
        switch (type) {
            case USUAL:
                return bodyRepository.findByType(AppearanceType.usual);
            case RARE:
                return bodyRepository.findByType(AppearanceType.rare);
            default:
                return bodyRepository.findAllByOrderByTypeAsc();
        }
    }

    public List<Ears> getEarsList(String type) {
        switch (type) {
            case USUAL:
                return earsRepository.findByType(AppearanceType.usual);
            case RARE:
                return earsRepository.findByType(AppearanceType.rare);
            default:
                return (List<Ears>) earsRepository.findAll();
        }
    }

    public List<Eyebrows> getEyebrowsList(String type) {
        switch (type) {
            case USUAL:
                return eyebrowsRepository.findByType(AppearanceType.usual);
            case RARE:
                return eyebrowsRepository.findByType(AppearanceType.rare);
            default:
                return (List<Eyebrows>) eyebrowsRepository.findAll();
        }
    }

    public List<EyeColor> getEyeColorList(String type) {
        switch (type) {
            case USUAL:
                return eyeColorRepository.findByType(AppearanceType.usual);
            case RARE:
                return eyeColorRepository.findByType(AppearanceType.rare);
            default:
                return (List<EyeColor>) eyeColorRepository.findAll();
        }
    }

    public List<Eyes> getEyesList(String type) {
        switch (type) {
            case USUAL:
                return eyesRepository.findByType(AppearanceType.usual);
            case RARE:
                return eyesRepository.findByType(AppearanceType.rare);
            default:
                return (List<Eyes>) eyesRepository.findAll();
        }
    }

    public List<HairColor> getHairColorList(String type) {
        switch (type) {
            case USUAL:
                return hairColorRepository.findByType(AppearanceType.usual);
            case RARE:
                return hairColorRepository.findByType(AppearanceType.rare);
            default:
                return (List<HairColor>) hairColorRepository.findAll();
        }
    }

    public List<HairStyle> getHairStyleList(String type) {

        return (List<HairStyle>) hairStyleRepository.findAll();

    }

    public List<HairType> getHairTypeList(String type) {
        switch (type) {
            case USUAL:
                return hairTypeRepository.findByType(AppearanceType.usual);
            case RARE:
                return hairTypeRepository.findByType(AppearanceType.rare);
            default:
                return (List<HairType>) hairTypeRepository.findAll();
        }
    }

    public List<Head> getHeadList(String type) {
        switch (type) {
            case USUAL:
                return headRepository.findByType(AppearanceType.usual);
            case RARE:
                return headRepository.findByType(AppearanceType.rare);
            default:
                return (List<Head>) headRepository.findAll();
        }
    }

    public List<Height> getHeightList(String type) {
        switch (type) {
            case USUAL:
                return heightRepository.findByType(AppearanceType.usual);
            case RARE:
                return heightRepository.findByType(AppearanceType.rare);
            default:
                return (List<Height>) heightRepository.findAll();
        }
    }

    public List<Mouth> getMouthList(String type) {
        switch (type) {
            case USUAL:
                return mouthRepository.findByType(AppearanceType.usual);
            case RARE:
                return mouthRepository.findByType(AppearanceType.rare);
            default:
                return (List<Mouth>) mouthRepository.findAll();
        }
    }

    public List<Nose> getNoseList(String type) {
        switch (type) {
            case USUAL:
                return noseRepository.findByType(AppearanceType.usual);
            case RARE:
                return noseRepository.findByType(AppearanceType.rare);
            default:
                return (List<Nose>) noseRepository.findAll();
        }
    }

    public List<SkinColor> getSkinColorList(String type) {
        switch (type) {
            case USUAL:
                return skinColorRepository.findByType(AppearanceType.usual);
            case RARE:
                return skinColorRepository.findByType(AppearanceType.rare);
            default:
                return (List<SkinColor>) skinColorRepository.findAll();
        }
    }

    // =================================================================
    public RaceAppearanceForm fillRaceAppearanceForm(RaceAppearanceForm form) {
        form.setBodyList((ArrayList<Body>) getBodyList(ALL));
        form.setEarsList((ArrayList<Ears>) getEarsList(ALL));
        form.setEyebrowsList((ArrayList<Eyebrows>) getEyebrowsList(ALL));
        form.setEyeColorList((ArrayList<EyeColor>) getEyeColorList(ALL));
        form.setEyesList((ArrayList<Eyes>) getEyesList(ALL));
        form.setHairColorList((ArrayList<HairColor>) getHairColorList(ALL));
        form.setHairTypeList((ArrayList<HairType>) getHairTypeList(ALL));
        form.setHeadList((ArrayList<Head>) getHeadList(ALL));
        form.setHeightList((ArrayList<Height>) getHeightList(ALL));
        form.setMouthList((ArrayList<Mouth>) getMouthList(ALL));
        form.setNoseList((ArrayList<Nose>) getNoseList(ALL));
        form.setSkinColorList((ArrayList<SkinColor>) getSkinColorList(ALL));
        return form;
    }
}
