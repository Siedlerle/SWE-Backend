package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.QuestionAnsweredByUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface is used to access the QuestionAnswerByUsers in the database.
 *
 * @author Fabian Eilber
 */
public interface QuestionAnswerByUserRepository extends JpaRepository<QuestionAnsweredByUser, Long> {
}
