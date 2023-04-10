package com.eventmaster.backend.EventManagement;


import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {}

