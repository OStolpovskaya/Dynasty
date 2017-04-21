package dyn.service;

import dyn.model.Buff;
import dyn.model.BuffType;
import dyn.model.Character;
import dyn.repository.BuffRepository;
import dyn.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OM on 15.03.2017.
 */
@Service
public class BuffService {
    @Autowired
    BuffRepository buffRepository;

    @Autowired
    private CharacterRepository characterRepository;


    public List<Buff> getBuffListForCharacter(Long characterId) {
        Character character = characterRepository.findOne(characterId);
        List<Buff> resultBuffs = new ArrayList<>();
        if (character != null) {
            List<Buff> buffList = buffRepository.findByType(BuffType.usual);
            for (Buff buff : buffList) {
                if (!character.getBuffs().contains(buff) && !character.getBuffs().contains(buff.getContradictory())) {
                    resultBuffs.add(buff);
                }
            }

        }
        return resultBuffs;
    }
}
