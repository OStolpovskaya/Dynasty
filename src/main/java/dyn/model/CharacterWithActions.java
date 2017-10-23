package dyn.model;

import dyn.service.CareerService;
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



}
