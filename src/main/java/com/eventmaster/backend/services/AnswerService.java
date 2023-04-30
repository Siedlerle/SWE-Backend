package com.eventmaster.backend.services;

import com.eventmaster.backend.entities.Answer;
import com.eventmaster.backend.entities.Question;
import com.eventmaster.backend.entities.QuestionAnsweredByUser;
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
    private final QuestionService questionService;
    private final UserService userService;
    private final QuestionAnsweredByUserService questionAnsweredByUserService;

    public AnswerService(AnswerRepository answerRepository,
                         QuestionService questionService,
                         UserService userService,
                         QuestionAnsweredByUserService questionAnsweredByUserService) {
        this.answerRepository = answerRepository;
        this.questionService = questionService;
        this.userService = userService;
        this.questionAnsweredByUserService = questionAnsweredByUserService;
    }

    private enum questionType{
        multipleChoice
    }

    public List<Answer> addAnswer(List<Answer> answers, long userId){
        List<Answer> returns = new ArrayList<Answer>();
        answers.forEach(a -> {
            Question q = a.getQuestion();
            if (q.getTypeVal().equals(questionType.multipleChoice.toString())) {
                List<Answer> ans = this.answerRepository
                        .findByQuestion_idAndText(q.getId(), a.getText());
                ans.get(0).setAmount(ans.get(0).getAmount() + 1);
                returns.add(this.answerRepository.save(ans.get(0)));
            } else {
                Answer answer = new Answer();
                answer.setText(a.getText());
                answer.setQuestion(questionService.findById(q.getId()));
                answer.setAmount(1);
                returns.add(this.answerRepository.save(answer));
            }
        });
        returns.forEach(r -> {
            QuestionAnsweredByUser qU = new QuestionAnsweredByUser();
            qU.setUser(userService.getUserById(userId));
            qU.setQuestion(r.getQuestion());
            questionAnsweredByUserService.savAnswerByUser(qU);
        });
        return returns;
    }
}
