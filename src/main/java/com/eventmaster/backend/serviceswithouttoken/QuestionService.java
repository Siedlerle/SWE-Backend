package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.Question;
import com.eventmaster.backend.repositories.QuestionAnswerByUserRepository;
import com.eventmaster.backend.repositories.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of questions
 *
 * @author Fabian Eilber
 */

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionAnswerByUserRepository questionAnswerByUserRepository;

    public QuestionService(QuestionRepository questionRepository,
                           QuestionAnswerByUserRepository questionAnswerByUserRepository){
        this.questionRepository = questionRepository;
        this.questionAnswerByUserRepository = questionAnswerByUserRepository;
    }

    //Todo Fehlende implementierung einbauen
    public List<Question> getUnfinishedQuestionsByUser(long eventId, long userId){

        return null;
    }

    /**
     * Checks if a question has been answered by a user
     * @param q Question object
     * @param userId Id of the user
     * @return Boolean to tell if User has answered question or not
     */
    private boolean hasNotAnswered(Question q, long userId) {
        return this.questionAnswerByUserRepository.findByQuestion_idAndUser_id(q.getId(), userId).isEmpty();
    }
}
