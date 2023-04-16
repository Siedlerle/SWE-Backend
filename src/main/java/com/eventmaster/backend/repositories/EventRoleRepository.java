package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.EventRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface is used to access the EventRoles in the database.
 *
 * @author Fabian Eilber
 */
public interface EventRoleRepository extends JpaRepository<EventRole, Long > {
    EventRole findByRole(String role);
}
