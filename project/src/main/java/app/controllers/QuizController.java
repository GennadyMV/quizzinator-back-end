package app.controllers;

import app.domain.Quiz;
import app.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class QuizController {
    @Autowired
    private QuizRepository quizRepo;
    
    @RequestMapping(value = "/quiz")
    public String getQuizzes(Model model) {
        model.addAttribute("quizzes", quizRepo.findAll());
        return "quiz";
    }
    
    @ResponseBody
    @RequestMapping(value = "/quiz/{id}", produces="application/json")
    public Quiz getQuiz(@PathVariable(value = "id") Long id) {
        Quiz k = quizRepo.findOne(id);
        
        return k;
    }
    
    @RequestMapping(value = "/quiz", method = RequestMethod.POST)
    public String newQuiz(@RequestParam(required = true) String question) {
        Quiz k = new Quiz();
        k.setQuestion(question);
        Long id = quizRepo.save(k).getId();
        
        return "redirect:/quiz/" + id;
    }
}