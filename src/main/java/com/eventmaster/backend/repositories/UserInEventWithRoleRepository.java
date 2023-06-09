package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.EventRole;
import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.entities.UserInEventWithRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This interface is used to access the UserInEventWithRoles in the database.
 *
 * @author Fabian Eilber
 */
public interface UserInEventWithRoleRepository extends JpaRepository<UserInEventWithRole, Long> {
    List<UserInEventWithRole> findByUser_Id(long userId);
    List<UserInEventWithRole> findByEvent_Id(long eventId);
    UserInEventWithRole findByUserAndEvent(User user, Event event);
    UserInEventWithRole findByUser_IdAndEvent_Id(long userId, long eventId);
    UserInEventWithRole findByEventAndEventRole(Event event, EventRole eventRole);
    //List<UserInEventWithRole> findByEvent_Id(long eventId);
    Boolean existsByUserAndEvent(User user, Event event);
    Boolean existsByUser_IdAndEvent_Id(long userId, long eventId);
}
