package com.eventmaster.backend.UserManagement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * A class which handles the HTTP-requests for users.
 *
 * @author Fabian Unger
 */

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Gets a user which will be added to the database and returns it back.
     *
     * @param user User which will be added to the database.
     * @return User which has been added to the database.
     */
    @PostMapping("/registration")
    public ResponseEntity<Boolean> createUser(@RequestBody User user)
    {
        return ResponseEntity.ok(userService.creatUser(user));
    }

    /**
     * Gets a mail and searches for the user with this mail in the database.
     * Returns the user.
     *
     * @param mail Mail of the user which has to be searched.
     * @return User
     */
    @PostMapping("/get-user/{mail}")
    public ResponseEntity<User> getUserByMail(@PathVariable String mail)
    {
        return ResponseEntity.ok(userService.getUserByMail(mail));
    }
}
