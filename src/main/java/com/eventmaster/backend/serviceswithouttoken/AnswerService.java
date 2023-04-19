package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.Answer;
import com.eventmaster.backend.entities.Question;
import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.repositories.AnswerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of answers
 *
 * @author Fabian Eilber
 */
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository){
        this.answerRepository = answerRepository;
    }

    public String answerQuestion(List<Answer> answers, long userId){
        try {

            List<Answer> returns = new ArrayList<Answer>();
            answers.forEach(a->{
                Question q = a.getQuestion();

            });


            return "Question answered successfully" ;
        }catch (Exception e) {
            e.printStackTrace();
            return "Question couldn´t be answered";
        }
    }

}
