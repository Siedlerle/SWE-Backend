package com.eventmaster.backend.services;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.Question;
import com.eventmaster.backend.repositories.QuestionRepository;
import org.springframework.stereotype.Service;


public class QuestionService {

    /*
    private final QuestionRepository questionRepository;

    private final EventService eventService;
    public QuestionService(QuestionRepository questionRepository, EventService eventService) {
        this.questionRepository = questionRepository;
        this.eventService = eventService;
    }

    public void createQuestion(long eventId, Question question){
        Event event = eventService.getEventById(eventId);
        question.setEvent(event);
        questionRepository.save(question);
    }

*/

}
