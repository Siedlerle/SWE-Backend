package com.eventmaster.backend.UserManagement;

import com.eventmaster.backend.EventManagement.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * A user is added to the databse
     * @param user User which is being added
     * @return Boolen as status for succes
     */
    public boolean createUser(User user) {
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * A user is searched in the database
     * @param mail Mail from User which is searched for
     * @return UserObject
     */
    public User getUserByMail(String mail){
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers)
        {
            if (user.getEmailAdress().equals(mail))
            {
                long id = user.getId();
                return userRepository.findById(id).orElse(null);
            }
        }
        return null;
    }


}
