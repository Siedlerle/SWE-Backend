package com.eventmaster.backend.serviceswithouttoken;
import com.eventmaster.backend.entities.User;
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
}
