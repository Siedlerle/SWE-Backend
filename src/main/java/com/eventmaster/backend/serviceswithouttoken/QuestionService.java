package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.Question;
import com.eventmaster.backend.repositories.QuestionAnswerByUserRepository;
import com.eventmaster.backend.repositories.QuestionRepository;
import com.eventmaster.backend.serviceswithouttoken.EventService;
import org.springframework.stereotype.Service;

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

    private final EventService eventService;

    public QuestionService(QuestionRepository questionRepository,
                           QuestionAnswerByUserRepository questionAnswerByUserRepository,
                           EventService eventService){
        this.questionRepository = questionRepository;
        this.questionAnswerByUserRepository = questionAnswerByUserRepository;
        this.eventService = eventService;
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

    /**
     * Creates a Questionnaire
     * @param eventId Id of the event for which the questionnaire is created
     * @param question The questionnaire to be saved
     * @return success or failure depending on result
     */
    public String createQuestion(long eventId, Question question){
        Event event = eventService.getEventById(eventId);
        question.setEvent(event);
        try {
            questionRepository.save(question);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }

    }
}
