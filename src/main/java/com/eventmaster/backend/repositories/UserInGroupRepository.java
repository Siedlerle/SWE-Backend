package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.UserInGroup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface is used to access the UserInGroups in the database.
 *
 * @author Fabian Eilber
 */
public interface UserInGroupRepository extends JpaRepository<UserInGroup, Long> {
}
