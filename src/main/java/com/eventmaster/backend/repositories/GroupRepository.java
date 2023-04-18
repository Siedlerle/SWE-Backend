package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface is used to access the Groups in the database.
 *
 * @author Fabian Unger
 */
public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findById(long groupId);
}
