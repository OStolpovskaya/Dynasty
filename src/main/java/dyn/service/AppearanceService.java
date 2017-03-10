package dyn.service;

import dyn.model.appearance.Eyes;
import dyn.model.appearance.Head;
import dyn.model.appearance.Height;
import dyn.model.appearance.SkinColor;
import dyn.repository.appearance.EyesRepository;
import dyn.repository.appearance.HeadRepository;
import dyn.repository.appearance.HeightRepository;
import dyn.repository.appearance.SkinColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by OM on 11.03.2017.
 */
@Service
public class AppearanceService {
    @Autowired
    EyesRepository eyesRepository;
    @Autowired
    HeadRepository headRepository;
    @Autowired
    HeightRepository heightRepository;
    @Autowired
    SkinColorRepository skinColorRepository;

    public Height getRandomRareHeight() {
        return heightRepository.getRandomRare();
    }

    public Head getRandomRareHead() {
        return headRepository.getRandomRare();
    }

    public Eyes getRandomRareEyes() {
        return eyesRepository.getRandomRare();
    }

    public SkinColor getRandomRareSkinColor() {
        return skinColorRepository.getRandomRare();
    }
}
