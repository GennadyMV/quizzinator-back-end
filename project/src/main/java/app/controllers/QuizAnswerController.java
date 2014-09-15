package app.controllers;

import app.domain.QuizAnswer;
import app.repositories.QuizAnswerRepository;
import app.repositories.QuizRepository;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "quizAnswers")
public class QuizAnswerController {
    
    @Autowired
    private QuizAnswerRepository quizAnswerRepository;
    
    @Autowired
    private QuizRepository quizRepository;
    
    @Transactional
    @RequestMapping(value = "/{quizId}", method = RequestMethod.POST, consumes = "application/json")
    public List<QuizAnswer> newAnswer(@PathVariable Long quizId, @Valid @RequestBody QuizAnswer quizAnswer) {
        
        Long answerId = quizAnswerRepository.save(quizAnswer).getId();
        quizRepository.findOne(quizId).getQuizAnswers().add(quizAnswer);
        
        List<QuizAnswer> quizAnswers = new ArrayList<QuizAnswer>();
        quizAnswers.add(quizRepository.findOne(quizId).getQuizAnswers().get(0));
        quizAnswers.add(quizRepository.findOne(quizId).getQuizAnswers().get(1));
        
        return quizAnswers;
    }
    
    @RequestMapping(value = "/{answerId}", method = RequestMethod.GET, produces = "application/json")
    public QuizAnswer getAnswer(@PathVariable Long answerId) {
        
        return quizAnswerRepository.findOne(answerId);
    }
}