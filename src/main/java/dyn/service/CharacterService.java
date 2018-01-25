package dyn.service;

import dyn.model.Character;
import dyn.model.Family;
import dyn.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OM on 15.03.2017.
 */
@Service
public class CharacterService {

    @Autowired
    AppearanceService app;
    @Autowired
    CareerService careerService;
    @Autowired
    private CharacterRepository characterRepository;

    public List<Character> getLevelCharactersAndSonsWifes(Family family) {
        List<Character> malesWithSpouse = characterRepository.findByFamilyAndLevelAndSexAndSpouseIsNotNull(family, family.getLevel(), "male");
        List<Character> wifesOfMales = new ArrayList<>();
        for (Character character : malesWithSpouse) {
            wifesOfMales.add(character.getSpouse());
        }
        List<Character> femalesWithoutSpouse = characterRepository.findByFamilyAndLevelAndSexAndSpouseIsNull(family, family.getLevel(), "female");
        List<Character> femaleWithoutSpouseAndNotFiancee = new ArrayList<>();
        for (Character character : femalesWithoutSpouse) {
            if (!character.isFiancee()) {
                femaleWithoutSpouseAndNotFiancee.add(character);
            }
        }

        List<Character> resultList = new ArrayList<>();
        resultList.addAll(malesWithSpouse);
        resultList.addAll(wifesOfMales);
        resultList.addAll(femaleWithoutSpouseAndNotFiancee);

        return resultList;
    }

    public String getNameForNewChild(Character child) {
        if (child.getSex().equals("male")) {
            return characterRepository.getRandomNameMale();
        } else {
            return characterRepository.getRandomNameFemale();
        }
    }

    public Character cloneCharacter(Family family, Character character) {
        Character clone = new Character();

        clone.setFamily(family);

        clone.setFather(character.getFather());
        clone.setSex(character.getSex());
        generateName(clone);
        clone.setRace(character.getRace());
        clone.setLevel(character.getLevel());

        clone.setCareer(careerService.copyCareer(character.getCareer()));

        clone.setBody(character.getBody());
        clone.setEars(character.getEars());
        clone.setEyebrows(character.getEyebrows());
        clone.setEyeColor(character.getEyeColor());
        clone.setEyes(character.getEyes());
        clone.setHairColor(character.getHairColor());
        clone.setHairType(character.getHairType());
        generateHairStyle(clone);
        clone.setHead(character.getHead());
        clone.setHeight(character.getHeight());
        clone.setMouth(character.getMouth());
        clone.setNose(character.getNose());
        clone.setSkinColor(character.getSkinColor());
        return clone;
    }

    public void generateHairStyle(Character character) {
        character.setHairStyle(app.getRandomHairStyle(character.getSex(), character.getHairType()));
    }

    public void generateName(Character character) {
        if (character.getSex().equals("male")) {
            character.setName(characterRepository.getRandomNameMale());
        } else {
            character.setName(characterRepository.getRandomNameFemale());
        }
    }
}
