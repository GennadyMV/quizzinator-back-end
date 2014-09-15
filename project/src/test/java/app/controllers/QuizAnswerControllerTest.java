package app.controllers;

import app.repositories.QuizAnswerRepository;
import app.repositories.QuizRepository;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class QuizAnswerControllerTest {
    
    @Autowired
    private QuizAnswerRepository quizAnswerRepository;
    
    @Autowired QuizRepository quizRepository;
    
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    @Test
    public void testAddAnswer() throws Exception {
        String jsonQuiz = "";
        
        this.mockMvc.perform(post("/quizAnswers/1").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
        
        
    }
    
    @Test
    public void testGetAnswer() throws Exception {
        String jsonQuiz = "";
        
        this.mockMvc.perform(get("/quizAnswers/1").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
    }
}
