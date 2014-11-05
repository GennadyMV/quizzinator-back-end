package app.controllers;

import app.Application;
import app.models.UsersReviewModel;
import app.repositories.PeerReviewRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
        JsonParser jp = new JsonParser();
        
        for (int i = 0; i < jp.parse(content).getAsJsonArray().size(); i++) {
            Long answerId = TestHelper.getLongByKeyAndIndexFromJsonArray(content, "id", i);
            assertTrue(answerId.equals(aId2) || answerId.equals(aId3));
        }
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

        JsonParser jsonParser = new JsonParser();
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

        JsonParser jsonParser = new JsonParser();
        JsonElement e = jsonParser.parse(response.getContentAsString()).getAsJsonArray();

        assertEquals(4, e.getAsJsonArray().size());
    }

    @Test
    @DirtiesContext
    public void testAnswersReturnedForReviewIsAsMuchAsPossible() throws Exception {
        MockHttpServletResponse response;
        JsonParser jsonParser = new JsonParser();
        
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
        JsonParser jp = new JsonParser();
        
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
        
        assertEquals(3, jp.parse(response.getContentAsString()).getAsJsonArray().size());
        
        response = mockMvc.perform(
                get("/quiz/" + quizId + "/review_answers")
                .param("username", "user0")
                .param("count", "10")).andReturn().getResponse();
        
        assertEquals(10, jp.parse(response.getContentAsString()).getAsJsonArray().size());
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
        JsonParser jp = new JsonParser();
        JsonArray ja = jp.parse(content).getAsJsonArray();
        
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
    public void badAnswerQuizCombination() throws Exception {
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
    public void badAnswerReviewCombination() throws Exception {
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
    public void badRatingValue() throws Exception {
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
    
    @Test
    @DirtiesContext
    public void testGetAnswerReviewsFaultyParameters() throws Exception {
        TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true, 2);
        TestHelper.addQuizWithOneQuestion(mockMvc, "quiz2", "question1", true, 2);
       
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", 1L);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user2", 2L);
        
        TestHelper.addAReview(mockMvc, 1L, 1L, "reviewer_guy", "good job!");
        TestHelper.addAReview(mockMvc, 2L, 2L, "reviewer_guy", "good job!");
        
        mockMvc.perform(get("/quiz/1/answer/2/review")).andExpect(status().is4xxClientError());
    }
    
    
}
