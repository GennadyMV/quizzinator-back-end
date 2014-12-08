package app.controllers;

import app.domain.Quiz;
import app.domain.QuizAnswer;
import app.models.UserData;
import app.repositories.QuizRepository;
import app.services.QuizService;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "quiz")
public class QuizController {
    @Autowired
    private QuizRepository quizRepo;
    
    @Autowired
    private QuizService quizService;
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, produces="application/json")
    @Transactional
    public List<Quiz> getQuizzes() {
        List<Quiz> quizes = new ArrayList<Quiz>();
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        
        long count = quizRepo.count();
        for (long i = 1; i <= count; i++) {
            Quiz quiz = quizRepo.findOne(i);
            
            if (quiz.getOwner().equals(name)) {
                quizes.add(quiz);
            }
        }
        
        return quizes;
    }
    
    @ResponseBody
    @RequestMapping(value = "/{id}", produces="application/json", method = RequestMethod.GET)
    @Transactional
    public Quiz getQuiz(@PathVariable(value = "id") Long id, @RequestParam(value = "username", required = false) String username) {
        Quiz q;
        if (username == null) {
            q = quizRepo.findOne(id);
        } else {
            q = quizService.getQuizForUsername(id, username);
        }
        
        return q;
    }
    
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @Transactional
    public String newQuiz(@Valid @RequestBody Quiz quiz) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String owner = auth.getName();
        quiz.setOwner(owner);
        
        Long id = quizRepo.save(quiz).getId();
        
        return "redirect:/quiz/" + id;
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.POST, consumes = "application/json")
    public String editQuiz(@Valid @RequestBody Quiz newQuiz, @PathVariable Long id) {
        quizRepo.save(newQuiz);
        
        return "redirect:/quiz/" + newQuiz.getId();
    }
    
    @Transactional
    @RequestMapping(value = "/{quizId}/placeholder", method = RequestMethod.POST, consumes = "application/json")
    public String newPlaceholderAnswer(
            @PathVariable Long quizId, 
            @RequestBody String quizAnswer, 
            HttpServletRequest request) {
        
        quizService.addPlaceHolderAnswer(quizAnswer, quizId);
        return "redirect:/quiz/" + quizId;
    }
    
    @Transactional
    @ResponseBody
    @RequestMapping(value = "/{quizId}/placeholder", method = RequestMethod.GET, produces = "application/json")
    public List<QuizAnswer> getPlaceholderAnswers(@PathVariable Long quizId) {
        return quizService.getPlaceholderAnswers(quizId);
    }
    
    @RequestMapping(value = "/{quizId}/clone", method = RequestMethod.POST)
    public String cloneQuiz(@PathVariable Long quizId) {
        Quiz newQuiz = new Quiz();
        Quiz quiz = quizRepo.findOne(quizId);
        
        newQuiz.setIsOpen(quiz.getIsOpen());
        newQuiz.setItems(quiz.getItems());
        newQuiz.setReviewable(quiz.isReviewable());
        newQuiz.setTitle(quiz.getTitle());
        quizRepo.save(newQuiz);
        
        return "redirect:/quiz/" + quizId;
    }
    
    @Transactional
    @ResponseBody
    @RequestMapping(value = "/{quizId}/userData", method = RequestMethod.GET, produces = "application/json")
    public List<UserData> getUserData(@PathVariable Long quizId) {
        return quizService.getUserData(quizId);
    }
}