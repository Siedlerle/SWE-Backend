package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
