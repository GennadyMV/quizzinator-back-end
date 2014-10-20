package app.controllers;

import app.domain.QuizAnswer;
import app.exceptions.NotFoundException;
import app.models.ReviewResponseModel;
import app.repositories.QuizAnswerRepository;
import app.repositories.QuizRepository;
import app.services.QuizService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class QuizAnswerController {
    @Autowired
    private QuizAnswerRepository answerRepo;
    
    @Autowired
    private QuizRepository quizRepo;
    
    @Autowired
    private QuizService quizService;
    
    @Transactional
    @ResponseBody
    @RequestMapping(value = "/quiz/{quizId}/answer", method = RequestMethod.POST, consumes = "application/json")
    public ReviewResponseModel newAnswer(
            @PathVariable Long quizId, 
            @Valid @RequestBody QuizAnswer quizAnswer, 
            HttpServletRequest request) {
        
        quizAnswer.setIp(request.getRemoteAddr());
        return quizService.submitAnswer(quizAnswer, quizId);
    }
    
    @Transactional
    @ResponseBody
    @RequestMapping(value = "/quiz/{quizId}/answer/{answerId}/improve", method = RequestMethod.POST, consumes = "application/json")
    public ReviewResponseModel improveAnswer(
            @PathVariable Long quizId, 
            @PathVariable Long answerId, 
            @Valid @RequestBody QuizAnswer quizAnswer, 
            HttpServletRequest request) {
        
        quizAnswer.setIp(request.getRemoteAddr());
        return quizService.improveAnswer(quizAnswer, quizId, answerId);
    }
    
    @Transactional
    @ResponseBody
    @RequestMapping(value = "/quiz/{quizId}/answer/{answerId}",
            method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public QuizAnswer getAnswer(@PathVariable Long quizId, @PathVariable Long answerId) {
        QuizAnswer quizAnswer = answerRepo.findOne(answerId);
        if (quizAnswer == null) {
            throw new NotFoundException();
        }
        
        return quizAnswer;
    }
    
    @Transactional
    @ResponseBody
    @RequestMapping(value = "/quiz/{quizId}/answer", method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public List<QuizAnswer> getAnswers(@PathVariable Long quizId) {
        return answerRepo.findByQuiz(quizRepo.findOne(quizId));
    }
    
    @Transactional
    @ResponseBody
    @RequestMapping(value = "/answer", method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public List<QuizAnswer> getAllAnswers() {
        List<QuizAnswer> answers = answerRepo.findAll();
        return answers;
    }
    
    @Transactional
    @ResponseBody
    @RequestMapping(value = "/quiz/{quizId}/answer/{answerId}", method = RequestMethod.DELETE,
            produces = "application/json; charset=UTF-8")
    public QuizAnswer deleteAnswer(@PathVariable Long quizId, @PathVariable Long answerId) {
        return quizService.deleteAnswer(quizId, answerId);
    }
}