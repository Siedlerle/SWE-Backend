package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.QuestionAnsweredByUser;
import com.eventmaster.backend.repositories.QuestionAnswerByUserRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionAnsweredByUserService {

    private final QuestionAnswerByUserRepository questionAnswerByUserRepository;


    public QuestionAnsweredByUserService(QuestionAnswerByUserRepository questionAnswerByUserRepository) {
        this.questionAnswerByUserRepository = questionAnswerByUserRepository;
    }

    public void savAnswerByUser(QuestionAnsweredByUser questionAnsweredByUser){
        questionAnswerByUserRepository.save(questionAnsweredByUser);
    }
}
