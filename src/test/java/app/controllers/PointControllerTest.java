package app.controllers;

import app.Application;
import app.models.QuizPointModel;
import app.domain.User;
import app.models.UserPointModel;
import app.repositories.PeerReviewRepository;
import app.repositories.QuizAnswerRepository;
import app.repositories.ReviewRatingRepository;
import app.repositories.UserRepository;
import com.google.gson.Gson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class PointControllerTest {
    
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    
    @Autowired
    private UserRepository userRepo;
    
    private Gson gson;
    
    private User user1;
    private User user2;
    
    public PointControllerTest() {
    }
    
    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        gson = new Gson();
        
        initQuiz();
    }
    
    @Test
    @DirtiesContext
    public void testGetPointsForUser() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/points/user/" + user1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        
        UserPointModel userPoint = gson.fromJson(mvcResult.getResponse().getContentAsString(),
                                                 UserPointModel.class);
        
        assertTrue(userPoint.getUsername().equals(user1.getName()));
        assertEquals(1, userPoint.getReviewCount().intValue());
        assertEquals(1, userPoint.getAnswerCount().intValue());
        assertEquals(1, userPoint.getRatingCount().intValue());
    }
    
    @Test
    @DirtiesContext
    public void testGetPointsForQuiz() throws Exception {
        initOverlappingPoints();
        
        MvcResult mvcResult = this.mockMvc.perform(get("/points/quiz/1")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andReturn();
        
        QuizPointModel quizPoint = gson.fromJson(mvcResult.getResponse().getContentAsString(),
                                                 QuizPointModel.class);
        
        assertEquals(1L, quizPoint.getQuiz().getId().longValue());
        assertEquals(2, quizPoint.getAnswerers().size());
        assertEquals(2, quizPoint.getReviewers().size());
        assertEquals(2, quizPoint.getRaters().size());
        assertTrue(quizPoint.getAnswerers().contains(user1.getName()));
        assertTrue(quizPoint.getAnswerers().contains(user2.getName()));
        assertTrue(quizPoint.getReviewers().contains(user1.getName()));
        assertTrue(quizPoint.getReviewers().contains(user2.getName()));
        assertTrue(quizPoint.getRaters().contains(user1.getName()));
        assertTrue(quizPoint.getRaters().contains(user2.getName()));
    }
    
    private void initQuiz() throws Exception {
        String jsonQuiz = "{\"title\":\"testquiz2\",\"items\":\"["
                + "{\\\"question\\\":\\\"testquestion1\\\",\\\"item_type\\\":\\\"open_question\\\",\\\"$$hashKey\\\":\\\"003\\\"},"
                + "{\\\"question\\\":\\\"testquestion2\\\",\\\"item_type\\\":\\\"open_question\\\",\\\"$$hashKey\\\":\\\"006\\\"}"
                + "]\"}";
        this.mockMvc.perform(post("/quiz").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
        
        initAnswers();
    }
    
    private void initAnswers() throws Exception {
        String jsonAnswer = 
                "{\"answer\":\"[{"
                + "\\\"question\\\":\\\"testquestion1\\\","
                + "\\\"value\\\":\\\"blablaaa\\\"},"
                + "{\\\"question\\\":\\\"testquestion2\\\","
                + "\\\"value\\\":\\\"faaaag\\\"}]\","
                + "\"user\":\"esko\"}";
        mockMvc.perform(post("/quiz/1/answer").content(jsonAnswer).contentType(MediaType.APPLICATION_JSON));
        
        jsonAnswer = 
                "{\"answer\":\"[{"
                + "\\\"question\\\":\\\"testquestion1\\\","
                + "\\\"value\\\":\\\"baumtsss\\\"},"
                + "{\\\"question\\\":\\\"testquestion2\\\","
                + "\\\"value\\\":\\\"hoho\\\"}]\","
                + "\"user\":\"asko\"}";
        mockMvc.perform(post("/quiz/1/answer").content(jsonAnswer).contentType(MediaType.APPLICATION_JSON));
        
        initReviews();
    }
    
    private void initReviews() throws Exception {
        user1 = userRepo.findAll().get(0);
        user2 = userRepo.findAll().get(1);
        
        String jsonReview = 
                "{\"reviewer\":\"asko\"," +
                "\"review\":\"hyvahyva\"}";
        mockMvc.perform(post("/quiz/1/answer/1/review").content(jsonReview)
                                                       .contentType(MediaType.APPLICATION_JSON));
        
        jsonReview = 
                "{\"reviewer\":\"esko\"," +
                "\"review\":\"surkea esitys\"}";
        mockMvc.perform(post("/quiz/1/answer/2/review").content(jsonReview)
                                                       .contentType(MediaType.APPLICATION_JSON));
        
        initReviewRatings();
    }
    
    private void initReviewRatings() throws Exception {
        mockMvc.perform(post("/quiz/1/answer/1/review/1/rate")
                .param("userhash", user1.getHash())
                .param("rating", "1"))
                .andExpect(status().isOk());
        
        mockMvc.perform(post("/quiz/1/answer/2/review/2/rate")
                .param("userhash", user2.getHash())
                .param("rating", "-1"))
                .andExpect(status().isOk());
    }
    
    private void initOverlappingPoints() throws Exception {
        String jsonAnswer = 
                "{\"answer\":\"[{"
                + "\\\"question\\\":\\\"testquestion1\\\","
                + "\\\"value\\\":\\\"blablaaa\\\"},"
                + "{\\\"question\\\":\\\"testquestion2\\\","
                + "\\\"value\\\":\\\"faaaag\\\"}]\","
                + "\"user\":\"asko\"}";
        mockMvc.perform(post("/quiz/1/answer").content(jsonAnswer).contentType(MediaType.APPLICATION_JSON));
        
        jsonAnswer = 
                "{\"answer\":\"[{"
                + "\\\"question\\\":\\\"testquestion1\\\","
                + "\\\"value\\\":\\\"baumtsss\\\"},"
                + "{\\\"question\\\":\\\"testquestion2\\\","
                + "\\\"value\\\":\\\"hoho\\\"}]\","
                + "\"user\":\"esko\"}";
        mockMvc.perform(post("/quiz/1/answer").content(jsonAnswer).contentType(MediaType.APPLICATION_JSON));
        
        String jsonReview = 
                "{\"reviewer\":\"asko\"," +
                "\"review\":\"hyvahyva\"}";
        mockMvc.perform(post("/quiz/1/answer/4/review").content(jsonReview)
                                                       .contentType(MediaType.APPLICATION_JSON));
        
        jsonReview = 
                "{\"reviewer\":\"esko\"," +
                "\"review\":\"surkea esitys\"}";
        mockMvc.perform(post("/quiz/1/answer/3/review").content(jsonReview)
                                                       .contentType(MediaType.APPLICATION_JSON));
        
        mockMvc.perform(post("/quiz/1/answer/4/review/3/rate")
                .param("userhash", user1.getHash())
                .param("rating", "1"))
                .andExpect(status().isOk());
        
        mockMvc.perform(post("/quiz/1/answer/3/review/4/rate")
                .param("userhash", user2.getHash())
                .param("rating", "-1"))
                .andExpect(status().isOk());
    }
}