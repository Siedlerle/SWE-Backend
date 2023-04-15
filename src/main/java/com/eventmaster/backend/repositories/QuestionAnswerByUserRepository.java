package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.QuestionAnsweredByUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionAnswerByUserRepository extends JpaRepository<QuestionAnsweredByUser, Long> {
}
