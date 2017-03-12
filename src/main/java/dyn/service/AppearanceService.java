package dyn.service;

import dyn.model.appearance.*;
import dyn.repository.appearance.EyesRepository;
import dyn.repository.appearance.HeadRepository;
import dyn.repository.appearance.HeightRepository;
import dyn.repository.appearance.SkinColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    EyesRepository eyesRepository;
    @Autowired
    HeadRepository headRepository;
    @Autowired
    HeightRepository heightRepository;
    @Autowired
    SkinColorRepository skinColorRepository;

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
}
