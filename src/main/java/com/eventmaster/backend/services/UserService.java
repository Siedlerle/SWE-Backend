package com.eventmaster.backend.services;

import com.eventmaster.backend.repositories.UserRepository;
import com.eventmaster.backend.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



}
