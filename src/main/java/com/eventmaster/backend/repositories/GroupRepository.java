package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This interface is used to access the Groups in the database.
 *
 * @author Fabian Unger
 */
public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findById(long groupId);
    List<Group> findByOrganisation_Id(long organisationId);
}
