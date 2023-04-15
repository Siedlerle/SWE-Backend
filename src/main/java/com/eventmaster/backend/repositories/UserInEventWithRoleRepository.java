package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.entities.UserInEventWithRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInEventWithRoleRepository extends JpaRepository<UserInEventWithRole, Long> {
    UserInEventWithRole findByUserAndEvent(User user, Event event);
    List<UserInEventWithRole> findByUser(User user);
}
