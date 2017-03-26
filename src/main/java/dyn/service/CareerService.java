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
        career.setEducation(educationRepository.findByName(Education.PRIMARY));
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

    public void inheritVocationAndSkills(Career child, Career father, Career mother) {
        double vocationPercentage = Math.random();
        if (vocationPercentage < 0.4) {
            child.setVocation(father.getVocation());
        } else if (vocationPercentage < 0.8) {
            child.setVocation(mother.getVocation());
        } else {
            child.setVocation(getRandomVocation());
        }

        child.setEducation(educationRepository.findByName(Education.PRIMARY));

        int incCoefficient = 3;

        child.setIntelligence(getRandomFromMinToMax(father.getIntelligence(), mother.getIntelligence()) + incCoefficient);
        child.setCharisma(getRandomFromMinToMax(father.getCharisma(), mother.getCharisma()) + incCoefficient);
        child.setStrength(getRandomFromMinToMax(father.getStrength(), mother.getStrength()) + incCoefficient);
        child.setCreativity(getRandomFromMinToMax(father.getCreativity(), mother.getCreativity()) + incCoefficient);
    }

    public int getRandomFromMinToMax(int first, int second) {
        int min = Math.min(first, second);
        int max = Math.max(first, second);
        int result = ThreadLocalRandom.current().nextInt(min, max + 1);
        return result;
    }

    public boolean mayImproveEducation(Career career) {
        String educationName;
        if (career.getEducation().getName().equals(Education.PRIMARY)) {
            educationName = Education.SECONDARY;
        } else if (career.getEducation().getName().equals(Education.SECONDARY)) {
            educationName = Education.HIGHER;
        } else {
            return false;
        }

        List<Profession> professions = professionRepository.findByVocationAndEducationAndIntelligenceLessThanEqualAndCharismaLessThanEqualAndStrengthLessThanEqualAndCreativityLessThanEqual(
                career.getVocation(),
                educationRepository.findByName(educationName),
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
        String educationName = Education.PRIMARY;
        if (career.getEducation().getName().equals(Education.PRIMARY)) {
            educationName = Education.SECONDARY;
        } else if (career.getEducation().getName().equals(Education.SECONDARY)) {
            educationName = Education.HIGHER;
        }

        career.setEducation(educationRepository.findByName(educationName));
    }
}