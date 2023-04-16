package com.eventmaster.backend.services;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.EventRole;
import com.eventmaster.backend.entities.UserInEventWithRole;
import com.eventmaster.backend.repositories.UserInEventWithRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of events
 *
 * @author Fabian Eilber
 */


public class UserInEventWithRoleService {

    private final UserInEventWithRoleRepository userInEventWithRoleRepository;

    public UserInEventWithRoleService(UserInEventWithRoleRepository userInEventWithRoleRepository) {
        this.userInEventWithRoleRepository = userInEventWithRoleRepository;
    }


}
