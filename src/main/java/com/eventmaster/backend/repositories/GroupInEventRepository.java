package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.GroupInEvent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface is used to access the GroupInEvents in the database.
 *
 * @author Fabian Unger
 */
public interface GroupInEventRepository extends JpaRepository<GroupInEvent, Long> {
}
