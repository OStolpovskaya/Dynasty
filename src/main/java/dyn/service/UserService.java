package dyn.service;

import dyn.model.User;
import dyn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by OM on 09.12.2017.
 */
@Service
public class UserService implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String userName = ((UserDetails) event.getAuthentication().
                getPrincipal()).getUsername();
        User user = userRepository.findByUserName(userName);
        user.setLastLoginDate(new Date());
        userRepository.save(user);
    }
}
