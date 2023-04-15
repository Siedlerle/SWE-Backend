package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.entities.UserInEventWithRole;
import com.eventmaster.backend.entities.UserInOrgaWithRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInOrgaWithRoleRepository extends JpaRepository<UserInOrgaWithRole, Long> {
    List<UserInOrgaWithRole> findByUser(User user);
}
