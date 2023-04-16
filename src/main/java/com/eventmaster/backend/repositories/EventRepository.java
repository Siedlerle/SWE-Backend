package com.eventmaster.backend.repositories;


import com.eventmaster.backend.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface is used to access the events in the database.
 *
 * @author Fabian Eilber
 */
public interface EventRepository extends JpaRepository<Event, Long> {}

