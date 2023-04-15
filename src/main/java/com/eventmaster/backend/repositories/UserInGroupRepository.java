package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.UserInGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInGroupRepository extends JpaRepository<UserInGroup, Long> {
}
