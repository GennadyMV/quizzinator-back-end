package app.controllers;

import app.domain.Quiz;
import app.repositories.OpenQuestionRepository;
import app.repositories.QuizRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class QuizController {
    @Autowired
    private QuizRepository quizRepo;
    @Autowired
    private OpenQuestionRepository openQuestionRepo;
    
    @ResponseBody
    @RequestMapping(value = "/quiz", produces="application/json")
    public List<Quiz> getQuizzes() {
        return quizRepo.findAll();
    }
    
    @ResponseBody
    @RequestMapping(value = "/quiz/{id}", produces="application/json")
    public Quiz getQuiz(@PathVariable(value = "id") Long id) {
        Quiz quiz = quizRepo.findOne(id);
        return quiz;
    }
    
    @RequestMapping(value = "/quiz", method = RequestMethod.POST, consumes = "application/json")
    public String newQuiz(@RequestBody Quiz quiz) {
        openQuestionRepo.save(quiz.getOpenQuestions());
        Long id = quizRepo.save(quiz).getId();
        
        return "redirect:/quiz/" + id;
    }
}
