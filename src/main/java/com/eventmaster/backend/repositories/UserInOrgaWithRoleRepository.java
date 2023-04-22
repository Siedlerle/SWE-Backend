package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.entities.UserInOrgaWithRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This interface is used to access the UserInOrgaWithRoles in the database.
 *
 * @author Fabian Eilber
 */
public interface UserInOrgaWithRoleRepository extends JpaRepository<UserInOrgaWithRole, Long> {
    List<UserInOrgaWithRole> findByUser(User user);
    UserInOrgaWithRole findByUser_IdAndOrganisation_Id(long userId, long orgaId);
    Boolean existsByUser_IdAndOrganisation_Id(long userId, long orgaId);
}
