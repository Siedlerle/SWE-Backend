package com.eventmaster.backend.serviceswithouttoken;
import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.entities.UserInEventWithRole;
import com.eventmaster.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of users
 *
 * @author Fabian Eilber
 */

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User getUserById(long userId) {
        return userRepository.findUserById(userId);
    }

    public String deleteUser(long userId) {
        try {

            User user = this.getUserById(userId);
            this.userRepository.deleteById(userId);

            return "User " + user.getFirstname() + " " + user.getLastname() + " deleted successfully" ;
        }catch (Exception e) {
            e.printStackTrace();
            return "User not deleted";
        }
    }
}
