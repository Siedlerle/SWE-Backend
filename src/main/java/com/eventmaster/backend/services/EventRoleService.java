package com.eventmaster.backend.services;


import com.eventmaster.backend.entities.EnumEventRole;
import com.eventmaster.backend.entities.EventRole;
import com.eventmaster.backend.repositories.EventRoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of eventroles
 *
 * @author Fabian Eilber
 * @author Fabian Unger
 */

@Service
public class EventRoleService {

    private final EventRoleRepository eventRoleRepository;

    public EventRoleService( EventRoleRepository eventRoleRepository){
        this.eventRoleRepository = eventRoleRepository;
    }

    /**
     * Creates the roles for events on build and stores them in the database if they don't already exist.
     */
    @PostConstruct
    public void init(){
        EnumEventRole roles;

        for (EnumEventRole role : EnumEventRole.values()) {
            if(findByRole(role) == null){
                EventRole staticEventRole = new EventRole();
                staticEventRole.setRole(role);
                staticEventRole.setId((long) role.ordinal());
                eventRoleRepository.save(staticEventRole);
            }
        }
    }

    /**
     * Searches for the EventRole for the associated name in the database and returns it.
     * @param role Name of role.
     * @return EventRole object if existing.
     */
    public EventRole findByRole(EnumEventRole role){
        try {
            return eventRoleRepository.findByRole(role);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Finds the EventRole for the id in the database and returns it.
     * @param id Id of the EventRole.
     * @return EventRole Object if existing.
     */
    public EventRole findById(long id) {
        try {
            return eventRoleRepository.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
