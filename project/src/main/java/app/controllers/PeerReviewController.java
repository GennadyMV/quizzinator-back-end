package app.controllers;

import app.domain.PeerReview;
import app.repositories.PeerReviewRepository;
import app.services.QuizService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import java.util.List;
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
@Api("")
public class PeerReviewController {
    @Autowired
    private PeerReviewRepository reviewRepo;
    
    @Autowired
    private QuizService quizService;
    
    @ApiOperation(value = "Get reviews", notes = "Get all reviews")
    @ResponseBody
    @RequestMapping(value = "/review", method = RequestMethod.GET, produces="application/json")
    @Transactional
    public List<PeerReview> getReviews() {
        return reviewRepo.findAll();
    }
    
    @ApiOperation(value = "Get reviews of an answer", notes = "Get reviews of an answer by answer id. Requires also right quiz id")
    @ResponseBody
    @RequestMapping(value = "/quiz/{quizId}/answer/{answerId}/review", method = RequestMethod.GET, produces="application/json")
    @Transactional
    public List<PeerReview> getAnswerReviews(@PathVariable Long quizId, @PathVariable Long answerId) {
        return quizService.getReviewsForAnAnswer(answerId, quizId);
    }
    
    @ResponseBody
    @ApiOperation(value = "Add new review", notes = "Adds a review for an answer by answer id. Requires also right quiz id")
    @RequestMapping(value = "/quiz/{quizId}/answer/{answerId}/review", method = RequestMethod.POST, consumes = "application/json")
    @Transactional
    public String newReview(
            @Valid @RequestBody PeerReview review,
            @PathVariable Long quizId,
            @PathVariable Long answerId) {
        quizService.saveNewReview(review, answerId, quizId);
        return "";
    }
}