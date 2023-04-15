package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.EventRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRoleRepository extends JpaRepository<EventRole, Long > {
    EventRole findByRole(String role);
}
