package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.OrgaRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface is used to access the OrgaRoles in the database.
 *
 * @author Fabian Unger
 */
public interface OrgaRoleRepository extends JpaRepository<OrgaRole, Long> {
}
