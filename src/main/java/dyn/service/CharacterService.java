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
}
