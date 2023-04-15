package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.EventSeries;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface is used to access the EventSerieses in the database.
 *
 * @author Fabian Eilber
 */
public interface EventSeriesRepository extends JpaRepository<EventSeries, Long> {
}
