package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.UserInOrgaWithRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrgaUserRoleRepository extends JpaRepository<UserInOrgaWithRole, Long> {
}
