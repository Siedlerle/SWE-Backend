package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface is used to access the Chats in the database.
 *
 * @author Fabian Eilber
 */
public interface ChatRepository extends JpaRepository<Chat, Long> {
}

