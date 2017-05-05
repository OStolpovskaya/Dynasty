package dyn.service;

import dyn.model.Character;
import dyn.model.career.Career;
import dyn.model.career.Education;
import dyn.model.career.Profession;
import dyn.model.career.Vocation;
import dyn.repository.career.EducationRepository;
import dyn.repository.career.ProfessionRepository;
import dyn.repository.career.VocationRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by OM on 23.03.2017.
 */
@Service
public class CareerService {
    private static final Logger logger = LogManager.getLogger(CareerService.class);

    @Autowired
    VocationRepository vocationRepository;

    @Autowired
    EducationRepository educationRepository;

    @Autowired
    ProfessionRepository professionRepository;

    public Career generateCareerForFounders() {
        Career career = new Career();
        career.setVocation(getRandomVocation());
        career.setIntelligence((int) (1 + Math.random() * 5));
        career.setCharisma((int) (1 + Math.random() * 5));
        career.setStrength((int) (1 + Math.random() * 5));
        career.setCreativity((int) (1 + Math.random() * 5));
        career.setEducation(educationRepository.findOne(Education.PRIMARY));
        career.setProfession(null);
        return career;
    }

    public Vocation getRandomVocation() {
        return vocationRepository.getRandom();
    }

    public void generateProfession(Character character) {
        Career career = character.getCareer();
        Profession profession = professionRepository.findFirstByVocationAndEducationAndIntelligenceLessThanEqualAndCharismaLessThanEqualAndStrengthLessThanEqualAndCreativityLessThanEqualOrderByLevelDesc(
                career.getVocation(),
                career.getEducation(),
                career.getIntelligence(),
                career.getCharisma(),
                career.getStrength(),
                career.getCreativity());
        if (profession == null) {
            logger.error("generateProfession return null! career = " + career);
        }
        career.setProfession(profession);
        /*
        List<Profession> professions = getAvailableProfessions(career);
        int index = new Random().nextInt(professions.size());
        Profession profession = professions.get(index);
        career.setProfession(profession);
        */
    }

    public List<Profession> getAvailableProfessions(Career career) {
        List<Profession> professions = professionRepository.findByVocationAndEducationAndIntelligenceLessThanEqualAndCharismaLessThanEqualAndStrengthLessThanEqualAndCreativityLessThanEqual(
                career.getVocation(),
                career.getEducation(),
                career.getIntelligence(),
                career.getCharisma(),
                career.getStrength(),
                career.getCreativity());
        return professions;
    }

    public void inheritVocationAndSkills(Career childCareer, Career fatherCareer, Career motherCareer) {
        double vocationPercentage = Math.random();
        if (vocationPercentage < 0.49) {
            childCareer.setVocation(fatherCareer.getVocation());
        } else if (vocationPercentage < 0.98) {
            childCareer.setVocation(motherCareer.getVocation());
        } else {
            childCareer.setVocation(getRandomVocation());
        }

        childCareer.setEducation(educationRepository.findOne(Education.PRIMARY));

        int incCoefficient = 2;

        childCareer.setIntelligence(getRandomFromMinToMax(fatherCareer.getIntelligence(), motherCareer.getIntelligence()) + incCoefficient);
        childCareer.setCharisma(getRandomFromMinToMax(fatherCareer.getCharisma(), motherCareer.getCharisma()) + incCoefficient);
        childCareer.setStrength(getRandomFromMinToMax(fatherCareer.getStrength(), motherCareer.getStrength()) + incCoefficient);
        childCareer.setCreativity(getRandomFromMinToMax(fatherCareer.getCreativity(), motherCareer.getCreativity()) + incCoefficient);
    }

    public int getRandomFromMinToMax(int first, int second) {
        int min = Math.min(first, second);
        int max = Math.max(first, second);
        int result = ThreadLocalRandom.current().nextInt(min, max + 1);
        return result;
    }

    public boolean mayImproveEducation(Career career) {
        long newEducation;
        if (career.getEducation().getId() == Education.PRIMARY) {
            newEducation = Education.SECONDARY;
        } else if (career.getEducation().getId() == Education.SECONDARY) {
            newEducation = Education.HIGHER;
        } else {
            return false;
        }

        List<Profession> professions = professionRepository.findByVocationAndEducationAndIntelligenceLessThanEqualAndCharismaLessThanEqualAndStrengthLessThanEqualAndCreativityLessThanEqual(
                career.getVocation(),
                educationRepository.findOne(newEducation),
                career.getIntelligence(),
                career.getCharisma(),
                career.getStrength(),
                career.getCreativity());
        if (professions.size() > 0) {
            return true;
        }
        return false;
    }

    public void improveEducation(Career career) {
        long educationId = Education.PRIMARY;
        if (career.getEducation().getId() == Education.PRIMARY) {
            educationId = Education.SECONDARY;
        } else if (career.getEducation().getId() == Education.SECONDARY) {
            educationId = Education.HIGHER;
        }

        career.setEducation(educationRepository.findOne(educationId));
    }

    public Vocation getVocation(long vocationId) {
        return vocationRepository.findOne(vocationId);
    }

    public Career copyCareer(Career career) {
        Career newCareer = new Career();
        newCareer.setVocation(career.getVocation());
        newCareer.setIntelligence(career.getIntelligence());
        newCareer.setCharisma(career.getCharisma());
        newCareer.setStrength(career.getStrength());
        newCareer.setCreativity(career.getCreativity());
        newCareer.setEducation(educationRepository.findOne(Education.PRIMARY));
        newCareer.setProfession(null);
        return newCareer;
    }

    public Career generateRandomCareer() {
        Career newCareer = new Career();
        newCareer.setVocation(getRandomVocation());
        newCareer.setIntelligence((int) (1 + Math.random() * 50));
        newCareer.setCharisma((int) (1 + Math.random() * 50));
        newCareer.setStrength((int) (1 + Math.random() * 50));
        newCareer.setCreativity((int) (1 + Math.random() * 50));
        newCareer.setEducation(educationRepository.findOne(Education.PRIMARY));
        newCareer.setProfession(null);
        return newCareer;
    }
}
