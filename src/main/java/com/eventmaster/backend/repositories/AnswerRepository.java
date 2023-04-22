package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This interface is used to access the Answers in the database.
 *
 * @author Fabian Eilber
 */
public interface AnswerRepository extends JpaRepository<Answer,Long> {
    List<Answer> findByQuestion_id(long question_id);
    List<Answer> findByQuestion_idAndText(long question_id, String text);
}
