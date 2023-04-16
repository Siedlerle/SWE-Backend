package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface is used to access the Questions in the database.
 *
 * @author Fabian Eilber
 */
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
