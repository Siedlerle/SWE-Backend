package com.eventmaster.backend.repositories;


import com.eventmaster.backend.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {}

