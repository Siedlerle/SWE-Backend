package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInEventWithRoleRepository extends JpaRepository<Event, Long> {
}
