package dyn.service;

import dyn.model.Family;
import dyn.model.User;
import dyn.model.UserNeighbor;
import dyn.repository.UserNeighborRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by OM on 26.12.2017.
 */
@Service
public class UserNeighborService {
    @Autowired
    private UserNeighborRepository userNeighborRepository;

    public boolean userHasThisNeighborLink(User user, Family family) {
        List<UserNeighbor> userNeighbors = userNeighborRepository.findAllByUserOrderByDateDesc(user);
        for (UserNeighbor userNeighbor : userNeighbors) {
            if (userNeighbor.getNeighborFamily() == family) {
                return true;
            }
        }
        return false;
    }

    public void addNewNeighbor(User user, User neighborUser, Family neighborFamily) {
        UserNeighbor userNeighbor = new UserNeighbor(user, neighborUser, neighborFamily);
        userNeighborRepository.save(userNeighbor);
    }

    public List<UserNeighbor> getNeighborsOfUser(User user) {
        List<UserNeighbor> userNeighbors = userNeighborRepository.findAllByUserOrderByDateDesc(user);
        return userNeighbors;
    }

    public UserNeighbor getNeighbor(Long neighborId) {
        UserNeighbor userNeighbor = userNeighborRepository.findOne(neighborId);
        return userNeighbor;
    }

    public void removeNeighbor(UserNeighbor neighbor) {
        userNeighborRepository.delete(neighbor);
    }
}
