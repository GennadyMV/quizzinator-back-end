package app.controllers;

import app.Application;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
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
        
        TestHelper.addAReview(mockMvc, 1L, 1L, "user2", "good answer!");
        
        MockHttpServletResponse response = this.mockMvc.perform(get("/quiz/1/answer/1/review"))
            .andExpect(status().isOk())
            .andReturn().getResponse();
        
        String content = response.getContentAsString();
        
        
        assertTrue(content.contains("\"id\":1"));
        assertTrue(content.contains("\"reviewer\":\"user2\""));
        assertTrue(content.contains("\"review\":\"good answer!\""));
    }
}
