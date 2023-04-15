package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.GroupInEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupInEventRepository extends JpaRepository<GroupInEvent, Long> {
}
