package dyn.model;

import dyn.service.CareerService;
import dyn.service.Const;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OM on 03.05.2017.
 */
public class CharacterWithActions {
    public Character character;
    public List<CharacterActions> characterActionsList = new ArrayList<>();
    @Autowired
    CareerService careerService;

    public void fillActionsForFather(Character father, List<Item> items) {
        character = father;
        // если есть items типа Клонировать персонажа, то добавляем действие
    }


    public void fillActionsForMother(Character mother, List<Item> items) {
        character = mother;
        // если есть items типа Клонировать персонажа, то добавляем действие
    }


    public void fillActionsForChild(Character child, List<Item> items) {
        character = child;
        Family family = character.getFamily();
        if (child.getSex().equals("male")) {
            if (character.getSpouse() == null && family.getPairsNum() < family.getHouse().getPairsNum()) {
                characterActionsList.add(CharacterActions.chooseFiancee);
            }
            /*if (character.getSpouse() != null){
                characterActionsList.add(БАФФЫ);
            }*/
        } else {
            if (character.getSpouse() == null && character.isFiancee() == false && family.getFianceeNum() < family.getHouse().getFianceeNum()) {
                characterActionsList.add(CharacterActions.putUpForFiancee);
            }
        }
        if (careerService.mayImproveEducation(character.getCareer())) {
            characterActionsList.add(CharacterActions.improveEducation);
        }

        for (Item item : items) {
            Long thingId = item.getProject().getThing().getId();
            if (thingId.equals(Const.THING_SKILL_IMPROVEMENT)) {
                characterActionsList.add(CharacterActions.skillImprovement);
            }
        }
    }
}
