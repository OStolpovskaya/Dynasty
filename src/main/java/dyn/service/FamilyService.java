package dyn.service;

import dyn.model.Family;
import dyn.model.User;
import dyn.repository.FamilyRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by OM on 09.12.2017.
 */
@Service
public class FamilyService {
    private static final Logger logger = LogManager.getLogger(FamilyService.class);
    @Autowired
    private FamilyRepository familyRepository;


    public Family getCurrentFamily(User user) {
        return familyRepository.findByUserAndCurrentIsTrue(user);
    }
}
