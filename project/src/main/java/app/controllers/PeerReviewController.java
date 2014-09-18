package app.controllers;

import app.domain.PeerReview;
import app.repositories.PeerReviewRepository;
import app.services.QuizService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PeerReviewController {
    @Autowired
    private PeerReviewRepository reviewRepo;
    
    @Autowired
    private QuizService quizService;
    
    @ResponseBody
    @RequestMapping(value = "/review", method = RequestMethod.GET, produces="application/json")
    public List<PeerReview> getReviews() {
        return reviewRepo.findAll();
    }
    
    @ResponseBody
    @RequestMapping(value = "/quiz/{quizId}/answer/{answerId}/review", method = RequestMethod.GET, produces="application/json")
    public List<PeerReview> getAnswerReviews(@PathVariable("quizId") Long quizId, @PathVariable("answerId") Long answerId) {
        return quizService.getReviewsForAnAnswer(answerId, quizId);
    }
    
    @RequestMapping(value = "/quiz/{quizId}/answer/{answerId}/review", method = RequestMethod.POST, consumes = "application/json")
    public String newReview(
            @Valid @RequestBody PeerReview review,
            @PathVariable("quizId") Long quizId,
            @PathVariable("answerId") Long answerId) {
        PeerReview r = quizService.saveNewReview(review, answerId, quizId);
        
        return "redirect:/quiz/" + r.getId();
    }
}