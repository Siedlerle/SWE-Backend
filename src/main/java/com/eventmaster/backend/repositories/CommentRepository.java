package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface is used to access the Comments in the database.
 *
 * @author Fabian Unger
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
