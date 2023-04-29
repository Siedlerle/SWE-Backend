package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.GroupInEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This interface is used to access the GroupInEvents in the database.
 *
 * @author Fabian Unger
 */
public interface GroupInEventRepository extends JpaRepository<GroupInEvent, Long> {
    List<GroupInEvent> findByEvent_Id(long eventId);
}
