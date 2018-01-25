package dyn.service;

import dyn.model.Character;
import dyn.model.Race;
import dyn.model.RaceAppearance;
import dyn.repository.CharacterRepository;
import dyn.repository.RaceAppearanceRepository;
import dyn.repository.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by OM on 15.03.2017.
 */
@Service
public class RaceService {
    @Autowired
    CharacterRepository characterRepository;
    @Autowired
    AppearanceService appearanceService;
    @Autowired
    private RaceRepository raceRepository;
    @Autowired
    private RaceAppearanceRepository raceAppearanceRepository;
    @Autowired
    private DriverManagerDataSource dataSource;

    public Race defineRace(Character character) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        StringBuilder query = new StringBuilder("SELECT id FROM `race_appearance` WHERE ").
                append(String.format("(`%1$s` = %2$s OR (`%1$s` is null AND %3$s)) AND ", "body", character.getBody().getId(), !character.getBody().isRare())).
                append(String.format("(`%1$s` = %2$s OR (`%1$s` is null AND %3$s)) AND ", "ears", character.getEars().getId(), !character.getEars().isRare())).
                append(String.format("(`%1$s` = %2$s OR (`%1$s` is null AND %3$s)) AND ", "eyebrows", character.getEyebrows().getId(), !character.getEyebrows().isRare())).
                append(String.format("(`%1$s` = %2$s OR (`%1$s` is null AND %3$s)) AND ", "eye_color", character.getEyeColor().getId(), !character.getEyeColor().isRare())).
                append(String.format("(`%1$s` = %2$s OR (`%1$s` is null AND %3$s)) AND ", "eyes", character.getEyes().getId(), !character.getEyes().isRare())).
                append(String.format("(`%1$s` = %2$s OR (`%1$s` is null AND %3$s)) AND ", "hair_color", character.getHairColor().getId(), !character.getHairColor().isRare())).
                append(String.format("(`%1$s` = %2$s OR (`%1$s` is null AND %3$s)) AND ", "hair_type", character.getHairType().getId(), !character.getHairType().isRare())).
                append(String.format("(`%1$s` = %2$s OR (`%1$s` is null AND %3$s)) AND ", "head", character.getHead().getId(), !character.getHead().isRare())).
                append(String.format("(`%1$s` = %2$s OR (`%1$s` is null AND %3$s)) AND ", "height", character.getHeight().getId(), !character.getHeight().isRare())).
                append(String.format("(`%1$s` = %2$s OR (`%1$s` is null AND %3$s)) AND ", "mouth", character.getMouth().getId(), !character.getMouth().isRare())).
                append(String.format("(`%1$s` = %2$s OR (`%1$s` is null AND %3$s)) AND ", "nose", character.getNose().getId(), !character.getNose().isRare())).
                append(String.format("(`%1$s` = %2$s OR (`%1$s` is null AND %3$s))", "skin_color", character.getSkinColor().getId(), !character.getSkinColor().isRare()));
        try {
            Long raceAppearanceId = jdbcTemplate.queryForObject(query.toString(), Long.class);
            RaceAppearance raceAppearance = raceAppearanceRepository.findOne(raceAppearanceId);
            return raceRepository.findOne(raceAppearance.getRace().getId());
        } catch (EmptyResultDataAccessException e) {
            if (character.hasRareFeatures()) {
                return raceRepository.findOne(Race.RACE_GM_HUMAN);
            } else {
                return raceRepository.findOne(Race.RACE_HUMAN);
            }

        }
    }

    public boolean isMarriageForbidden(Character character, Character fiancee) {
        Race characterRace = character.getRace();
        Race fianceeRace = fiancee.getRace();

        if (characterRace.getId() == Race.RACE_HUMAN || characterRace.getId() == Race.RACE_GM_HUMAN && fianceeRace.getId() >= Race.RACE_HIGH) {
            return true;
        }
        return false;
    }

    public List<Race> getAllRaces() {
        return raceRepository.findByIdGreaterThanOrderByName(2L);
    }

    public List<Race> getRaces() {
        return raceRepository.findAllByOrderByName();
    }

    public void turnCharacterToRace(Character character, Race newRace) {
        List<RaceAppearance> raceAppearanceList = raceAppearanceRepository.findByRace(newRace);
        RaceAppearance raceAppearance = raceAppearanceList.get(ThreadLocalRandom.current().nextInt(raceAppearanceList.size()));
        if (raceAppearance.getBody() == null) {
            if (character.getBody().isRare()) {
                character.setBody(appearanceService.getRandomBody(AppearanceService.USUAL));
            }
        } else {
            character.setBody(raceAppearance.getBody());
        }
        if (raceAppearance.getEars() == null) {
            if (character.getEars().isRare()) {
                character.setEars(appearanceService.getRandomEars(AppearanceService.USUAL));
            }
        } else {
            character.setEars(raceAppearance.getEars());
        }
        if (raceAppearance.getEyebrows() == null) {
            if (character.getEyebrows().isRare()) {
                character.setEyebrows(appearanceService.getRandomEyeBrows(AppearanceService.USUAL));
            }
        } else {
            character.setEyebrows(raceAppearance.getEyebrows());
        }
        if (raceAppearance.getEyeColor() == null) {
            if (character.getEyeColor().isRare()) {
                character.setEyeColor(appearanceService.getRandomEyeColor(AppearanceService.USUAL));
            }
        } else {
            character.setEyeColor(raceAppearance.getEyeColor());
        }
        if (raceAppearance.getEyes() == null) {
            if (character.getEyes().isRare()) {
                character.setEyes(appearanceService.getRandomEyes(AppearanceService.USUAL));
            }
        } else {
            character.setEyes(raceAppearance.getEyes());
        }
        if (raceAppearance.getHairColor() == null) {
            if (character.getHairColor().isRare()) {
                character.setHairColor(appearanceService.getRandomHairColor(AppearanceService.USUAL));
            }
        } else {
            character.setHairColor(raceAppearance.getHairColor());
        }
        if (raceAppearance.getHairType() == null) {
            if (character.getHairType().isRare()) {
                character.setHairType(appearanceService.getRandomHairType(AppearanceService.USUAL));
                character.setHairStyle(appearanceService.getRandomHairStyle(character.getSex(), character.getHairType()));
            }
        } else {
            character.setHairType(raceAppearance.getHairType());
            character.setHairStyle(appearanceService.getRandomHairStyle(character.getSex(), character.getHairType()));
        }
        if (raceAppearance.getHead() == null) {
            if (character.getHead().isRare()) {
                character.setHead(appearanceService.getRandomHead(AppearanceService.USUAL));
            }
        } else {
            character.setHead(raceAppearance.getHead());
        }
        if (raceAppearance.getHeight() == null) {
            if (character.getHeight().isRare()) {
                character.setHeight(appearanceService.getRandomHeight(AppearanceService.USUAL));
            }
        } else {
            character.setHeight(raceAppearance.getHeight());
        }
        if (raceAppearance.getMouth() == null) {
            if (character.getMouth().isRare()) {
                character.setMouth(appearanceService.getRandomMouth(AppearanceService.USUAL));
            }
        } else {
            character.setMouth(raceAppearance.getMouth());
        }
        if (raceAppearance.getNose() == null) {
            if (character.getNose().isRare()) {
                character.setNose(appearanceService.getRandomNose(AppearanceService.USUAL));
            }
        } else {
            character.setNose(raceAppearance.getNose());
        }
        if (raceAppearance.getSkinColor() == null) {
            if (character.getSkinColor().isRare()) {
                character.setSkinColor(appearanceService.getRandomSkinColor(AppearanceService.USUAL));
            }
        } else {
            character.setSkinColor(raceAppearance.getSkinColor());
        }
        Race race = defineRace(character);
        character.setRace(race);
        characterRepository.save(character);
    }
}
