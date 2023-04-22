package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This interface is used to access the Questions in the database.
 *
 * @author Fabian Eilber
 */
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByEventId(long eventId);
    Question findById(long questionId);
}
