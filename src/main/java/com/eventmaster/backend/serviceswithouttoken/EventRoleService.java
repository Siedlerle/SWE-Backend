package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.EventRole;
import com.eventmaster.backend.repositories.EventRoleRepository;
import org.springframework.stereotype.Service;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of eventroles
 *
 * @author Fabian Eilber
 */

@Service
public class EventRoleService {

    private final EventRoleRepository eventRoleRepository;

    public EventRoleService(EventRoleRepository eventRoleRepository) {
        this.eventRoleRepository = eventRoleRepository;

        String[] roles = {"ACCEPTED", "DECLINED", "REGISTERED", "UNREGISTERED"};

        EventRole role = new EventRole();

        for(int i = 0; i<roles.length; i++){
            role.setRole(roles[i]);
            role.setId(i);
            eventRoleRepository.save(role);
        }
    }

    public EventRole findByRole(String roleName){
       return eventRoleRepository.findByRole(roleName);
    }
}
