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

@Service
public class UserInEventWithRoleService {

    private final UserInEventWithRoleRepository userInEventWithRoleRepository;
    private final UserService userService;
    private final EventService eventService;

    public UserInEventWithRoleService(UserInEventWithRoleRepository userInEventWithRoleRepository, UserService userService, EventService eventService) {
        this.userInEventWithRoleRepository = userInEventWithRoleRepository;
        this.eventService = eventService;
        this.userService = userService;
    }


    //UserMethods
    public String registerForEvent(long eventId, String authToken){


        UserInEventWithRole userInEventWithRole = new UserInEventWithRole();

    }

    public String acceptEventInvitation(long eventId, String authToken){

    }

    public String declineEventInvitation(long eventId, String authToken){

    }

    public EventRole getRoleForEvent(long eventId, String authToken){

    }

    public List<Event> getRegisteredEventsForUser(String authToken){

    }

    public String unregisterFromEvent(Event event, String reason, String authToken){

    }

    public List<Event> getAllEventsForUser(String authToken){

    }
}
