package app.controllers;

import app.domain.Quiz;
import app.repositories.QuizRepository;
import app.services.QuizService;
import java.util.List;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
        return quizRepo.findAll();
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
    @RequestMapping(value = "/{id}/placeholder", method = RequestMethod.POST, consumes = "application/json")
    public String newPlaceholderAnswer(@RequestBody String quizAnswer, @PathVariable Long id) {
        quizService.addPlaceholderAnswer(quizAnswer, id);
        
        return "redirect:/quiz/" + id;
    }
}