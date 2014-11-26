package app.controllers;

import app.Application;
import app.domain.User;
import app.repositories.PeerReviewRepository;
import app.repositories.ReviewRatingRepository;
import app.repositories.UserRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.HashSet;
import org.json.JSONArray;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class PeerReviewControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Autowired
    private PeerReviewRepository reviewRepo;
    
    @Autowired
    private ReviewRatingRepository ratingRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    private final JsonParser jsonParser = new JsonParser();

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void controllerReturnsEmptyListWhenNothingAdded() throws Exception {
        this.mockMvc.perform(get("/review"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void controllerReturnsErrorIfQuizOrAnswerDoesntExist() throws Exception {
        this.mockMvc.perform(post("/quiz/999/answer/1/review"))
                .andExpect(status().is4xxClientError());

        this.mockMvc.perform(post("/quiz/1/answer/99/review"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DirtiesContext
    public void addingPeerReviewWorks() throws Exception {
        TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", 1L);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user2", 1L);
        TestHelper.addAReview(mockMvc, 1L, 1L, "user2", "good answer!");

        MvcResult mvcAnswer = this.mockMvc.perform(get("/quiz/1/answer/1/review"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(reviewRepo.findOne(1L).getReviewer().getName().equals("user2"));
        assertTrue(reviewRepo.findOne(1L).getReview().equals("good answer!"));
    }

    @Test
    @DirtiesContext
    public void leastReviewedAnswersAreOffered() throws Exception {
        Long qId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);

        Long aId1 = TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", qId);
        Long aId2 = TestHelper.addAnAnswer(mockMvc, "question1", "answer2", "user2", qId);
        Long aId3 = TestHelper.addAnAnswer(mockMvc, "question1", "answer3", "user3", qId);

        TestHelper.addAReview(mockMvc, qId, aId1, "user2", "good answer1!");
        TestHelper.addAReview(mockMvc, qId, aId2, "user2", "good answer2!");
        TestHelper.addAReview(mockMvc, qId, aId3, "user2", "good answer3!");
        TestHelper.addAReview(mockMvc, qId, aId1, "user2", "good answer4!");


        MockHttpServletResponse response = mockMvc.perform(
                get("/quiz/" + qId + "/review_answers")
                .param("username", "user4"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        String content = response.getContentAsString();
        
        for (int i = 0; i < jsonParser.parse(content).getAsJsonArray().size(); i++) {
            Long answerId = TestHelper.getLongByKeyAndIndexFromJsonArray(content, "id", i);
            assertTrue(answerId.equals(aId2) || answerId.equals(aId3));
        }
    }

    @Test
    @DirtiesContext
    public void userWithBiggerWeightGetsMoreFeedback() throws Exception {
        Long qId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);

        Long aId1 = TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", qId);
        Long aId2 = TestHelper.addAnAnswer(mockMvc, "question1", "answer2", "user2", qId);
        Long aId3 = TestHelper.addAnAnswer(mockMvc, "question1", "answer3", "user3", qId);
        
        User u = userRepo.findByName("user2");
        u.setReviewWeight(3.0);
        userRepo.save(u);

        TestHelper.addAReview(mockMvc, qId, aId1, "user5", "good answer1!");
        TestHelper.addAReview(mockMvc, qId, aId1, "user6", "good answer2!");
        TestHelper.addAReview(mockMvc, qId, aId2, "user4", "good answer3!");
        TestHelper.addAReview(mockMvc, qId, aId3, "user5", "good answer4!");


        MockHttpServletResponse response = mockMvc.perform(
                get("/quiz/" + qId + "/review_answers")
                .param("username", "newuser")
                .param("count", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        String content = response.getContentAsString();
        Long answerId = TestHelper.getLongByKeyAndIndexFromJsonArray(content, "id", 0);
        assertEquals(aId2, answerId);
        
        
        TestHelper.addAReview(mockMvc, qId, aId2, "user7", "good answer4!");

        response = mockMvc.perform(
                get("/quiz/" + qId + "/review_answers")
                .param("username", "newuser")
                .param("count", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        content = response.getContentAsString();
        answerId = TestHelper.getLongByKeyAndIndexFromJsonArray(content, "id", 0);
        assertEquals(aId2, answerId);
        
        
        TestHelper.addAReview(mockMvc, qId, aId2, "user8", "good answer4!");
        TestHelper.addAReview(mockMvc, qId, aId2, "user9", "good answer4!");

        response = mockMvc.perform(
                get("/quiz/" + qId + "/review_answers")
                .param("username", "newuser")
                .param("count", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        content = response.getContentAsString();
        answerId = TestHelper.getLongByKeyAndIndexFromJsonArray(content, "id", 0);
        
        //answer aId2 already has >3 answers so its turn for user1 or user3 with fewer answers
        assertNotEquals(aId2, answerId);
    }

    @Test
    @DirtiesContext
    public void testNumberOfAnswersReturnedForReviewIs2WhenRoundsIs1() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer2", "user2", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer3", "user3", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer4", "user4", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer5", "user5", quizId);

        
        MockHttpServletResponse response = mockMvc.perform(
                get("/quiz/" + quizId + "/review_answers")
                .param("username", "user6"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        JsonElement e = jsonParser.parse(response.getContentAsString()).getAsJsonArray();

        assertEquals(2, e.getAsJsonArray().size());
    }

    @Test
    @DirtiesContext
    public void testNumberOfAnswersReturnedForReviewIs4WhenRoundsIs2() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true, 2);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer2", "user2", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer3", "user3", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer4", "user4", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer5", "user5", quizId);

        MockHttpServletResponse response = mockMvc.perform(
                get("/quiz/" + quizId + "/review_answers")
                .param("username", "user6"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        JsonElement e = jsonParser.parse(response.getContentAsString()).getAsJsonArray();

        assertEquals(4, e.getAsJsonArray().size());
    }

    @Test
    @DirtiesContext
    public void testAnswersReturnedForReviewIsAsMuchAsPossible() throws Exception {
        MockHttpServletResponse response;
        
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true, 2);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", quizId);

        response = mockMvc.perform(get("/quiz/" + quizId + "/review_answers").param("username", "user0")).andReturn().getResponse();
        assertEquals(1, jsonParser.parse(response.getContentAsString()).getAsJsonArray().size());
        
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user2", quizId);
        response = mockMvc.perform(get("/quiz/" + quizId + "/review_answers").param("username", "user0")).andReturn().getResponse();
        
        assertEquals(2, jsonParser.parse(response.getContentAsString()).getAsJsonArray().size());
        
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user3", quizId);
        response = mockMvc.perform(get("/quiz/" + quizId + "/review_answers").param("username", "user0")).andReturn().getResponse();
        
        assertEquals(3, jsonParser.parse(response.getContentAsString()).getAsJsonArray().size());
        
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user4", quizId);
        response = mockMvc.perform(get("/quiz/" + quizId + "/review_answers").param("username", "user0")).andReturn().getResponse();
        
        assertEquals(4, jsonParser.parse(response.getContentAsString()).getAsJsonArray().size());
    }
    
    
    @Test
    @DirtiesContext
    public void testThetNumberOfAnswersReturnedForReviewWorks() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true, 2);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer2", "user2", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer3", "user3", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer4", "user4", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer5", "user5", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer2", "user2", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer3", "user3", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer4", "user4", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer5", "user5", quizId);
        
        MockHttpServletResponse response = mockMvc.perform(
                get("/quiz/" + quizId + "/review_answers")
                .param("username", "user0")
                .param("count", "3")).andReturn().getResponse();
        
        assertEquals(3, jsonParser.parse(response.getContentAsString()).getAsJsonArray().size());
        
        response = mockMvc.perform(
                get("/quiz/" + quizId + "/review_answers")
                .param("username", "user0")
                .param("count", "10")).andReturn().getResponse();
        
        assertEquals(10, jsonParser.parse(response.getContentAsString()).getAsJsonArray().size());
    }
    
    
    @Test
    @DirtiesContext
    public void testReviewRatingIsZeroIfReviewHasNotBeenRated() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
        
        //add answer
        Long answerId = TestHelper.addAnAnswer(mockMvc, "question1", "this is for review", "reviewme", quizId);
        
        //add review
        TestHelper.addAReview(mockMvc, quizId, 1L, "reviewer_guy", "good job!");
        
        //get review
        MockHttpServletResponse response = mockMvc.perform(get("/quiz/" + quizId + "/answer/" + answerId + "/review"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        
        Integer rating = TestHelper.getIntegerByKeyAndIndexFromJsonArray(response.getContentAsString(), "totalRating", 0);
        Integer expected = 0;
        assertEquals(expected, rating);
    }
    
    @Test
    @DirtiesContext
    public void testReviewRatingIs1IfPositiveRating() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
        String userhash = TestHelper.addAnswerAndGetUserhash(mockMvc, "question1", "this is for review and a good answer", "reviewme", quizId);
        TestHelper.addAReview(mockMvc, quizId, 1L, "reviewer_guy", "thats great, but use camelcase");
        
        //rate review as good
        mockMvc.perform(post("/quiz/"+quizId+"/answer/1/review/1/rate")
                .param("userhash", userhash)
                .param("rating", "1"))
                .andExpect(status().isOk());
        
        
        //get review
        MockHttpServletResponse response = mockMvc.perform(get("/quiz/"+quizId+"/answer/1/review"))
                .andReturn().getResponse();
        
        Integer rating = TestHelper.getIntegerByKeyAndIndexFromJsonArray(response.getContentAsString(), "totalRating", 0);
        Integer expected = 1;
        assertEquals(expected, rating);
    }
    
    @Test
    @DirtiesContext
    public void testReviewRatingIsMinus1IfNegativeRating() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
        String userhash = TestHelper.addAnswerAndGetUserhash(mockMvc, "question1", "this is for review and a good answer", "reviewme", quizId);
        TestHelper.addAReview(mockMvc, quizId, 1L, "reviewer_troll", "thats stupid u fool trololl");
        
        //rate review as bad
        mockMvc.perform(post("/quiz/"+quizId+"/answer/1/review/1/rate")
                .param("userhash", userhash)
                .param("rating", "-1"))
                .andExpect(status().isOk());
        
        
        //get review
        MockHttpServletResponse response = mockMvc.perform(get("/quiz/"+quizId+"/answer/1/review"))
                .andReturn().getResponse();
        
        Integer rating = TestHelper.getIntegerByKeyAndIndexFromJsonArray(response.getContentAsString(), "totalRating", 0);
        Integer expected = -1;
        assertEquals(expected, rating);
    }
    
    
    @Test
    @DirtiesContext
    public void testUserCantRateTheirOwnReviews() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
        Long answerId = TestHelper.addAnAnswer(mockMvc, "question1", "this is for review and a good answer", "user1", quizId);
        String user2hash = TestHelper.addAnswerAndGetUserhash(mockMvc, "question1", "this is for review and a good answer", "user2", quizId);
        
        //user2 reviews
        TestHelper.addAReview(mockMvc, quizId, answerId, "user2", "asdsda");
        
        //rate the review given by user2
        mockMvc.perform(post("/quiz/" + quizId + "/answer/" + answerId + "/review/1/rate")
            .param("userhash", user2hash)
            .param("rating", "1"))
            .andExpect(status().isForbidden());
    }
    
    @Test
    @DirtiesContext
    public void testGetUserReviews() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true, 2);
        String hash = TestHelper.addAnswerAndGetUserhash(mockMvc, "question1", "answer1", "user1", quizId);
        TestHelper.addAReview(mockMvc, quizId, 1L, "reviewer_guy", "good job!");
        TestHelper.addAReview(mockMvc, quizId, 1L, "bully", "u suck");
        TestHelper.addAReview(mockMvc, quizId, 1L, "fan", "luv u");
        
        quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz2", "question1", true, 2);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer2", "user1", quizId);
        TestHelper.addAReview(mockMvc, quizId, 2L, "reviewer_guy", "good job!");
        TestHelper.addAReview(mockMvc, quizId, 2L, "bully", "u suck");
        TestHelper.addAReview(mockMvc, quizId, 2L, "fan", "luv u");
        
        MvcResult result = mockMvc.perform(get("/reviews/" + hash)).andReturn();
        
        String content = result.getResponse().getContentAsString();
        JsonArray ja = jsonParser.parse(content).getAsJsonArray();
        
        assertEquals(2, ja.size());
        
        JsonObject jo = ja.get(0).getAsJsonObject();
        assertEquals(1, jo.get("quizId").getAsInt());
        assertEquals(1, jo.get("yourAnswer").getAsJsonObject().get("id").getAsInt());
        assertEquals(3, jo.get("reviews").getAsJsonArray().size());
        assertEquals("quiz1", jo.get("title").getAsString());
        
        jo = ja.get(1).getAsJsonObject();
        assertEquals(2, jo.get("quizId").getAsInt());
        assertEquals(2, jo.get("yourAnswer").getAsJsonObject().get("id").getAsInt());
        assertEquals(3, jo.get("reviews").getAsJsonArray().size());
        assertEquals("quiz2", jo.get("title").getAsString());
    }
    
    @Test
    @DirtiesContext
    public void testBadAnswerQuizCombination() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true, 2);
        String hash = TestHelper.addAnswerAndGetUserhash(mockMvc, "question1", "answer1", "user1", quizId);
        
        String jsonReview = 
                "{\"reviewer\":\"reviewer_guy\"," +
                "\"review\":\"good job!\"}";
        
        String url = "/quiz/" + quizId + "/answer/2/review";
        
        mockMvc.perform(post(url).content(jsonReview).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError());
        
        TestHelper.addAReview(mockMvc, quizId, 1L, "reviewer_guy", "good job!");
        
        mockMvc.perform(post("/quiz/"+quizId+"/answer/2/review/1/rate")
                .param("userhash", hash)
                .param("rating", "1"))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    @DirtiesContext
    public void testBadAnswerReviewCombination() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true, 2);
        String hash = TestHelper.addAnswerAndGetUserhash(mockMvc, "question1", "answer1", "user1", quizId);
        
        TestHelper.addAReview(mockMvc, quizId, 1L, "reviewer_guy", "good job!");
        
        mockMvc.perform(post("/quiz/1/answer/1/review/2/rate")
            .param("userhash", hash)
            .param("rating", "1"))
            .andExpect(status().is4xxClientError());
    }
    
    @Test
    @DirtiesContext
    public void testBadRatingValue() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true, 2);
        final String hash = TestHelper.addAnswerAndGetUserhash(mockMvc, "question1", "answer1", "user1", quizId);
        
        TestHelper.addAReview(mockMvc, quizId, 1L, "reviewer_guy", "good job!");
        
        mockMvc.perform(post("/quiz/1/answer/1/review/1/rate")
            .param("userhash", hash)
            .param("rating", "0"))
            .andExpect(status().is4xxClientError());
    }
    
    @Test
    @DirtiesContext
    public void testGetReviewsByQuizForRating() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true, 2);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user2", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user3", quizId);
        
        
        TestHelper.addAReview(mockMvc, quizId, 1L, "reviewer_guy", "good job!");
        TestHelper.addAReview(mockMvc, quizId, 2L, "reviewer_guy", "good job!");
        TestHelper.addAReview(mockMvc, quizId, 3L, "reviewer_guy", "good job!");
        
        MvcResult result = mockMvc.perform(get("/quiz/1/reviews")
                .param("reviewCount", "2")
                .param("username", "user1"))
                .andReturn();
        
        JSONArray array = new JSONArray(result.getResponse().getContentAsString());
        assertEquals(2, array.length());
        
        result = mockMvc.perform(get("/quiz/1/reviews")
                .param("reviewCount", "4")
                .param("username", "user2"))
                .andReturn();
        
        array = new JSONArray(result.getResponse().getContentAsString());
        assertEquals(3, array.length());
    }
    
    public void testGetAnswerReviewsFaultyParameters() throws Exception {
        TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true, 2);
        TestHelper.addQuizWithOneQuestion(mockMvc, "quiz2", "question1", true, 2);
       
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", 1L);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user2", 2L);
        
        TestHelper.addAReview(mockMvc, 1L, 1L, "reviewer_guy", "good job!");
        TestHelper.addAReview(mockMvc, 2L, 2L, "reviewer_guy", "good job!");
        
        mockMvc.perform(get("/quiz/1/answer/2/review")).andExpect(status().is4xxClientError());
    }
    
    @Test
    @DirtiesContext
    public void testReturnedPeerReviewsContainsNoDuplicates() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true, 2);
        Long answerId = TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", quizId);
        Long answerId2 = TestHelper.addAnAnswer(mockMvc, "question1", "answer2", "user0", quizId);
        
        TestHelper.addAReview(mockMvc, quizId, answerId, "user2", "way to go!"); //assume id = 1
        TestHelper.addAReview(mockMvc, quizId, answerId2, "user7", "yeah!"); //2
        
        //multiple ratings duplicated returned reviews at some point
        TestHelper.addAReviewRating(mockMvc, quizId, answerId, 1L, "user3", 1);
        TestHelper.addAReviewRating(mockMvc, quizId, answerId, 1L, "user4", 1);
        TestHelper.addAReviewRating(mockMvc, quizId, answerId, 1L, "user5", 1);
        TestHelper.addAReviewRating(mockMvc, quizId, answerId, 1L, "user6", -1);
        TestHelper.addAReviewRating(mockMvc, quizId, answerId2, 2L, "user11", -1);
        TestHelper.addAReviewRating(mockMvc, quizId, answerId2, 2L, "user8", 1);
        TestHelper.addAReviewRating(mockMvc, quizId, answerId2, 2L, "user9", -1);
        TestHelper.addAReviewRating(mockMvc, quizId, answerId2, 2L, "user10", -1);
        
        
        MvcResult result = mockMvc.perform(get("/quiz/" + quizId + "/reviews")
            .param("reviewCount", "5")
            .param("username", "user12"))
            .andExpect(status().isOk())
            .andReturn();
        
        String response = result.getResponse().getContentAsString();
        JsonArray ja = jsonParser.parse(response).getAsJsonArray();
        
        //only 2 should be found, but might be duplicated by an error
        assertEquals(2, ja.size());
        
        HashSet<Integer> reviewIds = new HashSet<Integer>();
        for (JsonElement je : ja) {
            Integer reviewId = je.getAsJsonObject().get("id").getAsInt();
            assertTrue(reviewIds.add(reviewId));
        }
    }
    
    @Test
    @DirtiesContext
    public void testGettingPeerReviewsWithoutAnsweringIsPossible() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true, 2);
        Long answerId = TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", quizId);
        
        
        TestHelper.addAReview(mockMvc, quizId, answerId, "reviewer_guy", "good job!");
        TestHelper.addAReview(mockMvc, quizId, answerId, "reviewer_guy2", "allright!");
        TestHelper.addAReview(mockMvc, quizId, answerId, "reviewer_guy3", "way to go!");
        TestHelper.addAReview(mockMvc, quizId, answerId, "reviewer_guy4", "that's the spirit!");
        
        MvcResult result = mockMvc.perform(get("/quiz/" + quizId + "/reviews")
            .param("reviewCount", "3")
            .param("username", "user2"))
            .andExpect(status().isOk())
            .andReturn();
        
        String response = result.getResponse().getContentAsString();
        JsonArray ja = jsonParser.parse(response).getAsJsonArray();
        
        assertEquals(3, ja.size());
    }
    
    @Test
    @DirtiesContext
    public void testReturnedPeerReviewsDontContainUsersOwnReviews() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true, 2);
        Long answerId = TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", quizId);
        Long answerId2 = TestHelper.addAnAnswer(mockMvc, "question1", "answer2", "user3", quizId);
        
        String expectedReview1 = "good job!";
        String expectedReview2 = "that's the spirit!";
        
        TestHelper.addAReview(mockMvc, quizId, answerId, "reviewer_guy", expectedReview1);
        TestHelper.addAReview(mockMvc, quizId, answerId2, "user2", "allright!");
        TestHelper.addAReview(mockMvc, quizId, answerId, "user2", "way to go!");
        TestHelper.addAReview(mockMvc, quizId, answerId2, "reviewer_guy4", expectedReview2);
        
        MvcResult result = mockMvc.perform(get("/quiz/" + quizId + "/reviews")
            .param("reviewCount", "3")
            .param("username", "user2"))
            .andExpect(status().isOk())
            .andReturn();
        
        String response = result.getResponse().getContentAsString();
        JsonArray ja = jsonParser.parse(response).getAsJsonArray();
        
        //only two should be available
        assertEquals(2, ja.size());
    
        String reviewer1 = ja.get(0).getAsJsonObject().get("reviewer").getAsString();
        String review1 = ja.get(0).getAsJsonObject().get("review").getAsString();
        String reviewer2 = ja.get(1).getAsJsonObject().get("reviewer").getAsString();
        String review2 = ja.get(1).getAsJsonObject().get("review").getAsString();
        
        assertTrue(reviewer1.equals("reviewer_guy") || reviewer1.equals("reviewer_guy4"));
        assertTrue(review1.equals(expectedReview1) || review1.equals(expectedReview2));
        
        assertTrue(reviewer2.equals("reviewer_guy") || reviewer2.equals("reviewer_guy4"));
        assertTrue(review2.equals(expectedReview1) || review2.equals(expectedReview2));

    }
    
    
    @Test
    @DirtiesContext
    public void testRatingSamePeerReviewReplacesOldRating() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true, 2);
        Long answerId = TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", quizId);
        
        TestHelper.addAReview(mockMvc, quizId, answerId, "reviewer_guy", "good job!");
        
        mockMvc.perform(post("/quiz/" + quizId + "/answer/" + answerId + "/review/1/rate")
            .param("username", "user1")
            .param("rating", "1"))
            .andExpect(status().isOk());
        
        mockMvc.perform(post("/quiz/" + quizId + "/answer/" + answerId + "/review/1/rate")
            .param("username", "user1")
            .param("rating", "-1"))
            .andExpect(status().isOk());
        
        
        MvcResult result = mockMvc.perform(get("/quiz/" + quizId + "/answer/" + answerId + "/review"))
            .andExpect(status().isOk())
            .andReturn();
        
        String response = result.getResponse().getContentAsString();
        
        //only one review is expected
        assertEquals(1, jsonParser.parse(response).getAsJsonArray().size());
        
        //jsonParser.parse(response).getAsJsonArray().get(0).getAsJsonObject().get("rateCount").getAsInt()
        assertEquals(1, jsonParser.parse(response).getAsJsonArray().get(0).getAsJsonObject().get("rateCount").getAsInt());
        
    }
    
    @Test
    @DirtiesContext
    public void canRateWithUsernameOrUserhash() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
        String userhash = TestHelper.addAnswerAndGetUserhash(mockMvc, "question1", "this is for review and a good answer", "reviewme", quizId);
        TestHelper.addAReview(mockMvc, quizId, 1L, "reviewer_guy", "thats great, but use camelcase");
        TestHelper.addAReview(mockMvc, quizId, 1L, "other_guy", "thats great, but use camelcase");
        
        //rate review as good
        mockMvc.perform(post("/quiz/"+quizId+"/answer/1/review/1/rate")
                .param("userhash", userhash)
                .param("rating", "1"))
                .andExpect(status().isOk());
        
        assertEquals(1, ratingRepo.count());
        
        mockMvc.perform(post("/quiz/"+quizId+"/answer/1/review/2/rate")
                .param("username", "masa")
                .param("rating", "1"))
                .andExpect(status().isOk());
        
        assertEquals(2, ratingRepo.count());
    }
    
    @Test
    @DirtiesContext
    public void cannotRateWithFaultyUser() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
        TestHelper.addAnswerAndGetUserhash(mockMvc, "question1", "this is for review and a good answer", "reviewme", quizId);
        TestHelper.addAReview(mockMvc, quizId, 1L, "reviewer_guy", "thats great, but use camelcase");
        TestHelper.addAReview(mockMvc, quizId, 1L, "other_guy", "thats great, but use camelcase");
        
        //rate review as good
        mockMvc.perform(post("/quiz/"+quizId+"/answer/1/review/1/rate")
                .param("userhash", "")
                .param("rating", "1"))
                .andExpect(status().is4xxClientError());
        
        assertEquals(0, ratingRepo.count());
    }
    
    @Test
    @DirtiesContext
    public void canGetPeerReviewsGivenToUserForOneQuiz() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true, 2);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user2", quizId);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user3", quizId);
        
        
        TestHelper.addAReview(mockMvc, quizId, 1L, "reviewer_guy", "good job!");
        TestHelper.addAReview(mockMvc, quizId, 1L, "reviewer_guy", "good job!");
        TestHelper.addAReview(mockMvc, quizId, 3L, "reviewer_guy", "good job!");
        
        MvcResult result = mockMvc.perform(get("/quiz/" + quizId  + "/myReviews")
                .param("username", "user1"))
                .andReturn();
        
        JSONArray array = new JSONArray(result.getResponse().getContentAsString());
        assertEquals(2, array.length());
        
        result = mockMvc.perform(get("/quiz/" + quizId  + "/myReviews")
                .param("username", "user3"))
                .andReturn();
        
        array = new JSONArray(result.getResponse().getContentAsString());
        assertEquals(1, array.length());
    }
}
