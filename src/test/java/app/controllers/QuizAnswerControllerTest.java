package app.controllers;

import app.Application;
import app.domain.Quiz;
import app.domain.QuizAnswer;
import app.repositories.QuizAnswerRepository;
import app.repositories.QuizRepository;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import static org.junit.Assert.*;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
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
        
        
        String jsonQuiz = "{\"title\":\"testquiz1\",\"reviewable\":\"true\",\"items\":\"[{}]\"}";
        
        this.mockMvc.perform(post("/quiz").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
        
        this.quiz = quizRepository.findAll().get(0); //new PageRequest(0, 1, Sort.Direction.DESC, "id")).getContent().get(0);
    }
    
    @Test
    @DirtiesContext
    public void testAddAnswer() throws Exception {
        String jsonQuiz = "{\"username\": \"matti\","
                         + "\"answer\": \"vastaus\"}";
        
        this.mockMvc.perform(post("/quiz/1/answer").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
        
        QuizAnswer quizAnswer = quizAnswerRepository.findOne(1L);
        
        Assert.assertEquals("matti", quizAnswer.getUser().getName());
        Assert.assertEquals("vastaus", quizAnswer.getAnswer());
    }
    
    @Test
    @DirtiesContext
    public void testPostAnswerReturnsTwoAnswers() throws Exception {
        String jsonQuiz = "{\"username\": \"eero\","
                         + "\"answer\": \"vastaus\"}";
        this.mockMvc.perform(post("/quiz/"+ quiz.getId() + "/answer")
                             .content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
        
        jsonQuiz = "{\"username\": \"masa\","
                         + "\"answer\": \"vastaus\"}";
        this.mockMvc.perform(post("/quiz/"+ quiz.getId() + "/answer")
                             .content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
        
        jsonQuiz = "{\"username\": \"kalevi\","
                         + "\"answer\": \"vastaus\"}";
        MvcResult mvcAnswer = this.mockMvc.perform(post("/quiz/"+ quiz.getId() + "/answer")
                             .content(jsonQuiz).contentType(MediaType.APPLICATION_JSON)).andReturn();
        
        Gson gson = new Gson();
        
        
        System.out.println("|||||||||||||||||||||||||||||||||||");
        System.out.println(mvcAnswer.getResponse().getContentAsString());
        System.out.println("|||||||||||||||||||||||||||||||||||");
        
        
        JSONObject obj = new JSONObject(mvcAnswer.getResponse().getContentAsString());
   
        assertEquals(2, new JSONArray(obj.getString("answers")).length());
    }
    
    @Test
    @DirtiesContext
    public void testCorrectNumberOfAnswers() throws Exception {
        String jsonAnswer = "{\"username\": \"ulla\","
                         + "\"answer\": \"vastaus\"}";
        
        for (int i = 0; i < 4; i++) {
            this.mockMvc.perform(post("/quiz/1/answer").content(jsonAnswer).contentType(MediaType.APPLICATION_JSON));
        }
        
        assertEquals(4, quizAnswerRepository.count());
    }
    
    @Test
    @DirtiesContext
    public void testGetAnswer() throws Exception {
        String jsonQuiz = "{\"username\":\"matti\","
                         + "\"answer\":\"vastaus\"}";
        
        this.mockMvc.perform(post("/quiz/1/answer").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
        
        MvcResult mvcAnswer = this.mockMvc.perform(get("/quiz/1/answer/1")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andReturn();
        
        Gson gson = new Gson();
        QuizAnswer answer = gson.fromJson(mvcAnswer.getResponse().getContentAsString(), QuizAnswer.class);
        
        QuizAnswer quizAnswer = quizAnswerRepository.findOne(1L);
        
        assertEquals(quizAnswer.getUser().getName(), answer.getUsername());
        assertEquals(answer.getAnswer(), quizAnswer.getAnswer());
    }
}