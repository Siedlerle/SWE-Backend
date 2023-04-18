package com.eventmaster.backend.serviceswithouttoken;


import com.eventmaster.backend.entities.EventRole;
import com.eventmaster.backend.repositories.EventRoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of eventroles
 *
 * @author Fabian Eilber
 */

@Service
public class EventRoleService {

    private final EventRoleRepository eventRoleRepository;

    public EventRoleService( EventRoleRepository eventRoleRepository){
        this.eventRoleRepository = eventRoleRepository;
    }

    @PostConstruct
    public void init(){
        String[] roles = {"ACCEPTED", "DECLINED", "REGISTERED", "UNREGISTERED"};

        for(int i=0; i<roles.length; i++){
            String rolName = roles[i];

            if(eventRoleRepository.findEventRoleByRoleName(rolName) == null){
                EventRole staticEventRole = new EventRole();
                staticEventRole.setRoleName(rolName);
                staticEventRole.setId((long) i);
                eventRoleRepository.save(staticEventRole);
            }
        }
    }

    public EventRole findByRole(String roleName){
        return eventRoleRepository.findEventRoleByRoleName(roleName);
    }
}
