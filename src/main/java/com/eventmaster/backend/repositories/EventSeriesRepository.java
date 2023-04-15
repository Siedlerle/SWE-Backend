package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.EventSeries;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventSeriesRepository extends JpaRepository<EventSeries, Long> {
}
