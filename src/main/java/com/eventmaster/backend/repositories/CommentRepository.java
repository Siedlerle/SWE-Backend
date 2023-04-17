package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This interface is used to access the Comments in the database.
 *
 * @author Fabian Unger
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByChatId(long chatId);

    Comment findCommentByChatId(long chatId);

}
