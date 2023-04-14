package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}

