package dyn.service;

import dyn.model.Character;
import dyn.model.Race;
import dyn.model.RaceAppearance;
import dyn.repository.RaceAppearanceRepository;
import dyn.repository.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

/**
 * Created by OM on 15.03.2017.
 */
@Service
public class RaceService {
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
                return raceRepository.findByName(Race.RACE_GM_HUMAN);
            } else {
                return raceRepository.findByName(Race.RACE_HUMAN);
            }

        }
    }
}
