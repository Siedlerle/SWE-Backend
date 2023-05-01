package com.eventmaster.backend.services;

import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.repositories.QuestionAnswerByUserRepository;
import com.eventmaster.backend.repositories.QuestionRepository;
import org.springframework.context.annotation.Lazy;
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
    private final EventService eventService;
    private final AnswerService answerService;

    public QuestionService(QuestionRepository questionRepository,
                           QuestionAnswerByUserRepository questionAnswerByUserRepository,
                           EventService eventService,
                           @Lazy AnswerService answerService){
        this.questionRepository = questionRepository;
        this.questionAnswerByUserRepository = questionAnswerByUserRepository;
        this.eventService = eventService;
        this.answerService = answerService;
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
     * @param questions The questionnaire to be saved
     * @return success or failure depending on result
     */
    public MessageResponse createQuestion(long eventId, List<Question> questions){
        try {
            Event event = eventService.getEventById(eventId);

            for (Question question : questions) {

                question.setEvent(event);
                questionRepository.save(question);

                if (question.getType().equals(EnumQuestionType.MULTIPLECHOICE)) {

                    for (Answer answer : question.getAnswers()) {
                        Answer a = new Answer();
                        a.setAmount(0);
                        a.setText(answer.getText());
                        a.setQuestion(question);
                        answerService.saveAnswer(a);
                    }
                }
            }

            return MessageResponse.builder()
                    .message("success")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message("failure")
                    .build();
        }

    }

    public Question findById(long questionId) {
        return questionRepository.findById(questionId);
    }
}
