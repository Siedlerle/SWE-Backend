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
    private final UserService userService;

    public QuestionService(QuestionRepository questionRepository,
                           QuestionAnswerByUserRepository questionAnswerByUserRepository,
                           EventService eventService,
                           @Lazy AnswerService answerService,
                           UserService userService){
        this.questionRepository = questionRepository;
        this.questionAnswerByUserRepository = questionAnswerByUserRepository;
        this.eventService = eventService;
        this.answerService = answerService;
        this.userService = userService;
    }

    /**
     * Returns all unfinished questions from user
     * @param eventId Id of the corresponding event
     * @param emailAdress EMail of the corresponding user
     * @return List of questions
     */
    public List<Question> getUnfinishedQuestionsByUser(long eventId, String emailAdress){
        try {

            User user = userService.getUserByMail(emailAdress);

            List<Question> questions = questionRepository.findByEventId(eventId);
            List<Question> toAnswer = new ArrayList<>();
            questions.stream()
                    .filter(q -> hasNotAnswered(q, user.getId()))
                    .forEach(q -> toAnswer.add(q));
            toAnswer.stream()
                    .filter(q -> q.getQuestionType().equals(EnumQuestionType.MULTIPLECHOICE))
                    .forEach(q -> q.setAnswerString(answerService
                                    .findByQuestionId(q.getId())
                                    .stream()
                                    .map(a -> a.getText())
                                    .toArray(String[]::new)
                            )
                    );
            return toAnswer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns all Answers to the questions in an event
     * @param eventId Id of the corresponding event
     * @return List of answers
     */
    public List<Answer> getAnswersToQuestions(long eventId){

        List<Question> questions = questionRepository.findByEventId(eventId);
        List<Answer> answers = new ArrayList<>();

        for (Question question:questions) {
            List<Answer> tmp = answerService.findByQuestionId(question.getId());
            for (Answer answer:tmp) {
                answers.add(answer);
            }
        }
        return answers;
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

                if(question.getQuestionType().equals(EnumQuestionType.MULTIPLECHOICE)){
                    System.out.println(question.getQuestionText());
                    for (String answer : question.getAnswerString()) {
                        Answer a = new Answer();
                        a.setAmount(0);
                        a.setText(answer);
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

    public List<Question> getAllQuestionsForEvent(long eventId){
        try {

            List<Question> questions = questionRepository.findByEventId(eventId);
            return questions;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Question findById(long questionId) {
        return questionRepository.findById(questionId);
    }
}
