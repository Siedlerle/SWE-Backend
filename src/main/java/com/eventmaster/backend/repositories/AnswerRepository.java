package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer,Long> {
}