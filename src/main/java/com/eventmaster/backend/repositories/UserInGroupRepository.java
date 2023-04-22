package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Group;
import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.entities.UserInGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This interface is used to access the UserInGroups in the database.
 *
 * @author Fabian Eilber
 */
public interface UserInGroupRepository extends JpaRepository<UserInGroup, Long> {
    UserInGroup findByUserAndGroup(User user, Group group);
    List<UserInGroup> findByUser(User user);
    List<UserInGroup> findByGroup(Group group);
}
