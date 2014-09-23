package app.controllers;

import app.Application;
import app.domain.Quiz;
import app.domain.QuizAnswer;
import app.repositories.QuizAnswerRepository;
import app.repositories.QuizRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.List;
import org.json.JSONObject;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
public class QuizAnswerControllerTest {
    @Autowired
    private QuizAnswerRepository quizAnswerRepository;
    
    @Autowired
    private QuizRepository quizRepository;
    
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    
    private Quiz quiz;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        
        
        String jsonQuiz = "{\"title\":\"testquiz1\",\"items\":\"["
                + "{}]\"}";
        
        this.mockMvc.perform(post("/quiz").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
        
        this.quiz = quizRepository.findAll().get((int)quizRepository.count() - 1);
    }
    
    @Test
    public void testAddAnswer() throws Exception {
        String jsonQuiz = "{\"user\": \"matti\", \"ip\": \"0.0.0.0\","
                         + "\"url\": \"http://www.joku.com/\", \"answer\": \"vastaus\"}";
        
        this.mockMvc.perform(post("/quiz/"+ quiz.getId() + "/answer").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
        
        QuizAnswer quizAnswer = quizAnswerRepository.findAll().get((int)quizAnswerRepository.count() - 1);
        
        Assert.assertEquals("matti", quizAnswer.getUser());
        Assert.assertEquals("0.0.0.0", quizAnswer.getIp());
        Assert.assertEquals("http://www.joku.com/", quizAnswer.getUrl());
        Assert.assertEquals("vastaus", quizAnswer.getAnswer());
    }
    
    /**
    @Test
    public void testPostAnswerReturnsTwoAnswers() throws Exception {
        String jsonQuiz = "{\"user\": \"eero\", \"ip\": \"0.0.0.0\","
                         + "\"url\": \"http://www.joku.com/\", \"answer\": \"vastaus\"}";
        
        MvcResult mvcAnswer = this.mockMvc.perform(post("/quiz/"+ quiz.getId() + "/answer")
                             .content(jsonQuiz).contentType(MediaType.APPLICATION_JSON)).andReturn();
        
        Gson gson = new Gson();
        List<QuizAnswer> answers = gson.fromJson(mvcAnswer.getResponse().getContentAsString(), List.class);
        
        assertEquals(2, answers.size());
    }
    **/
    
    @Test
    public void testCorrectNumberOfAnswers() throws Exception {
        String jsonQuiz = "{\"user\": \"ulla\", \"ip\": \"0.0.0.0\","
                         + "\"url\": \"http://www.joku.com/\", \"answer\": \"vastaus\"}";
        
        for (int i = 0; i < 4; i++) {
            this.mockMvc.perform(post("/quiz/"+ quiz.getId() + "/answer").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
        }
        
        //One is added in the previous test.
        Assert.assertEquals(5, quizAnswerRepository.count());
    }
    
    @Test
    public void testGetAnswer() throws Exception {
        MvcResult mvcAnswer = this.mockMvc.perform(get("/quiz/" + quiz.getId() +
                            "/answer/" + (quizAnswerRepository.count() - 1))
                            .contentType(MediaType.APPLICATION_JSON))
                            .andReturn();
        
        Gson gson = new Gson();
        QuizAnswer answer = gson.fromJson(mvcAnswer.getResponse().getContentAsString(), QuizAnswer.class);
        
        QuizAnswer quizAnswer = quizAnswerRepository.findAll().get((int)quizAnswerRepository.count() - 1);

        Assert.assertEquals(answer.getUser(), quizAnswer.getUser());
        Assert.assertEquals(answer.getIp(), quizAnswer.getIp());
        Assert.assertEquals(answer.getUrl(), quizAnswer.getUrl());
        Assert.assertEquals(answer.getAnswer(), quizAnswer.getAnswer());
    }
}