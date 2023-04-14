package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
