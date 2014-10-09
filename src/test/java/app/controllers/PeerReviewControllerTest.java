package app.controllers;

import app.Application;
import app.repositories.PeerReviewRepository;
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
        TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
        
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", 1L);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer2", "user2", 1L);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer3", "user3", 1L);
        
        TestHelper.addAReview(mockMvc, 1L, 1L, "user2", "good answer1!");
        TestHelper.addAReview(mockMvc, 1L, 2L, "user2", "good answer2!");
        TestHelper.addAReview(mockMvc, 1L, 3L, "user2", "good answer3!");
        TestHelper.addAReview(mockMvc, 1L, 1L, "user2", "good answer4!");
        
        
        String jsonAnswer = 
                "{\"answer\":\"[{"
                + "\\\"question\\\":\\\"question1\\\","
                + "\\\"value\\\":\\\"answer4\\\"}]\","
                + "\"user\":\"user4\"}";
        
        MockHttpServletResponse response = mockMvc.perform(post("/quiz/1/answer")
                .content(jsonAnswer).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        
        String content = response.getContentAsString();
        
        assertFalse(content.contains("\"id\":1"));
        assertTrue(content.contains("\"id\":2"));
        assertTrue(content.contains("\"id\":3"));
    }
}
