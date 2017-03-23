package dyn.service;

import dyn.model.career.Career;
import dyn.model.career.Education;
import dyn.repository.career.EducationRepository;
import dyn.repository.career.VocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by OM on 23.03.2017.
 */
@Service
public class CareerService {
    @Autowired
    VocationRepository vocationRepository;

    @Autowired
    EducationRepository educationRepository;

    public Career generateCareerForFounders() {
        Career career = new Career();
        career.setVocation(vocationRepository.getRandom());
        career.setIntelligence((int) (1 + Math.random() * 5));
        career.setCharisma((int) (1 + Math.random() * 5));
        career.setStrength((int) (1 + Math.random() * 5));
        career.setCreativity((int) (1 + Math.random() * 5));
        career.setEducation(educationRepository.findByName(Education.PRIMARY));
        career.setProfession(null);
        return career;
    }

}
