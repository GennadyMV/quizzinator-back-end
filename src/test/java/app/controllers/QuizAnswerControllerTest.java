package app.controllers;

import app.Application;
import app.domain.Quiz;
import app.domain.QuizAnswer;
import app.repositories.QuizAnswerRepository;
import app.repositories.QuizRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Date;
import org.json.JSONArray;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
        
        TestHelper.addQuizWithOneQuestion(mockMvc, "testquiz1", "testquestion1", true);
        
        this.quiz = quizRepository.findAll().get(0);
    }
    
    @Test
    @DirtiesContext
    public void testAddAnswer() throws Exception {
        String jsonQuiz = "{\"user\": \"matti\","
                         + "\"answer\": \"vastaus\"}";
        
        this.mockMvc.perform(post("/quiz/1/answer").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
        
        QuizAnswer quizAnswer = quizAnswerRepository.findOne(1L);
        
        Assert.assertEquals("matti", quizAnswer.getUser().getName());
        Assert.assertEquals("vastaus", quizAnswer.getAnswer());
    }
    
    @Test
    @DirtiesContext
    public void testPostAnswerReturnsNewAnswerModel() throws Exception {
        Long qId = quiz.getId();
        
        TestHelper.addAnAnswer(mockMvc, "q", "a", "user1", qId);
        TestHelper.addAnAnswer(mockMvc, "q", "a", "user2", qId);
        TestHelper.addAnAnswer(mockMvc, "q", "a", "user3", qId);
        
        String jsonAnswer = "{\"user\": \"user4\","
                         + "\"answer\": \"vastaus\"}";
        MvcResult mvcAnswer = this.mockMvc.perform(post("/quiz/"+ qId + "/answer")
                             .content(jsonAnswer).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        
        JsonParser jp = new JsonParser();
        JsonObject answerModel = jp.parse(mvcAnswer.getResponse().getContentAsString()).getAsJsonObject();
        assertTrue(answerModel.get("answer").isJsonObject());
        assertTrue(answerModel.get("userhash").isJsonPrimitive());
    }
    
    @Test
    @DirtiesContext
    public void testCorrectNumberOfAnswers() throws Exception {
        String jsonAnswer = "{\"user\": \"ulla\","
                         + "\"answer\": \"vastaus\"}";
        
        for (int i = 0; i < 4; i++) {
            this.mockMvc.perform(post("/quiz/1/answer").content(jsonAnswer).contentType(MediaType.APPLICATION_JSON));
        }
        
        assertEquals(4, quizAnswerRepository.count());
    }
    
    @Test
    @DirtiesContext
    public void testGetAnswer() throws Exception {
        TestHelper.addAnAnswer(mockMvc, "testikysymys", "testivastaus", "matti", 1L);
        
        MvcResult mvcAnswer = this.mockMvc.perform(get("/quiz/1/answer/1")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andReturn();
        
        String response = mvcAnswer.getResponse().getContentAsString();
        
        assertTrue(response.contains("\"user\":\"matti\""));
        assertTrue(response.contains("\\\"question\\\":\\\"testikysymys\\\""));
        assertTrue(response.contains("\\\"value\\\":\\\"testivastaus\\\""));
    }
    
    @Test
    @DirtiesContext
    public void testDeleteAnswer() throws Exception {
        Long quizId = quiz.getId();
        Long answer1Id = TestHelper.addAnAnswer(mockMvc, "testikysymys", "testivastaus1", "user1", quizId);
        Long answer2Id = TestHelper.addAnAnswer(mockMvc, "testikysymys", "testivastaus2", "user2", quizId);
        
        this.mockMvc.perform(delete("/quiz/" + quizId + "/answer/" + answer2Id)).andExpect(status().isOk());
        
        this.mockMvc.perform(get("/quiz/" + quizId + "/answer/" + answer2Id)).andExpect(status().isNotFound());
        this.mockMvc.perform(get("/quiz/" + quizId + "/answer/" + answer1Id)).andExpect(status().isOk());
    }
    
    @Test
    @DirtiesContext
    public void testDeleteAnswerWontBreakPreviousAnswerLink() throws Exception {
        Long quizId = quiz.getId();
        Long answer1Id = TestHelper.addAnAnswer(mockMvc, "testikysymys", "testivastaus1", "user1", quizId);
        Long answer2Id = TestHelper.addAnAnswer(mockMvc, "testikysymys", "testivastaus2", "user1", quizId);
        Long answer3Id = TestHelper.addAnAnswer(mockMvc, "testikysymys", "testivastaus3", "user1", quizId);
        
        this.mockMvc.perform(delete("/quiz/" + quizId + "/answer/" + answer2Id)).andExpect(status().isOk());
        
        String response = this.mockMvc.perform(get("/quiz/" + quizId + "/answer/" + answer1Id))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertNull(TestHelper.getLongByKeyFromJson(response, "previousAnswerId"));
        
        this.mockMvc.perform(get("/quiz/" + quizId + "/answer/" + answer2Id)).andExpect(status().isNotFound());
        
        response = this.mockMvc.perform(get("/quiz/" + quizId + "/answer/" + answer3Id))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        
        //last answers previousAnswerId should be fixed to point to the first answer
        assertEquals(answer1Id, TestHelper.getLongByKeyFromJson(response, "previousAnswerId"));
    }
    
    @Test
    @DirtiesContext
    public void testPreviousAnswerIdIsSetAfterSecondAnswer() throws Exception {
        Long quizId = quiz.getId();
        Long answer1Id = TestHelper.addAnAnswer(mockMvc, "testikysymys", "testivastaus1", "user1", quizId);
        Long answer2Id = TestHelper.addAnAnswer(mockMvc, "testikysymys", "testivastaus2", "user1", quizId);
        
        MvcResult mvcAnswer = this.mockMvc.perform(get("/quiz/" + quizId + "/answer/" + answer2Id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        
        String response = mvcAnswer.getResponse().getContentAsString();
        
        assertEquals(answer1Id, TestHelper.getLongByKeyFromJson(response, "previousAnswerId"));
    }
    
    @Test
    @DirtiesContext
    public void testPreviousAnswerIdIsNullFirstAnswer() throws Exception {
        Long quizId = quiz.getId();
        Long answer1Id = TestHelper.addAnAnswer(mockMvc, "testikysymys", "testivastaus2", "user1", quizId);
        
        MvcResult mvcAnswer = this.mockMvc.perform(get("/quiz/" + quizId + "/answer/" + answer1Id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        
        String response = mvcAnswer.getResponse().getContentAsString();
        
        assertNull(TestHelper.getStringByKeyFromJson(response, "previousAnswerId"));
    }
    
    @Test
    @DirtiesContext
    public void testAnswerDateIsSet() throws Exception {
        Long quizId = quiz.getId();
        Long answer1Id = TestHelper.addAnAnswer(mockMvc, "testikysymys", "testivastaus2", "user1", quizId);
        
        MvcResult mvcAnswer = this.mockMvc.perform(get("/quiz/" + quizId + "/answer/" + answer1Id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        
        String response = mvcAnswer.getResponse().getContentAsString();
        
        Long answerTimestamp = TestHelper.getLongByKeyFromJson(response, "answerDate");
        assertTrue(Math.abs(answerTimestamp-new Date().getTime())<60*1000);
    }
    
    @Test
    @DirtiesContext
    public void testGetAnswers() throws Exception {
        Long qId = quiz.getId();
        TestHelper.addAnAnswer(mockMvc, "q", "a", "user1", qId);
        TestHelper.addAnAnswer(mockMvc, "q", "a", "user2", qId);
        TestHelper.addAnAnswer(mockMvc, "q", "a", "user3", qId);
        
        TestHelper.addQuizWithOneQuestion(mockMvc, "testquiz2", "testquestion2", true);
        qId = 2L;
        TestHelper.addAnAnswer(mockMvc, "q", "a", "user1", qId);
        TestHelper.addAnAnswer(mockMvc, "q", "a", "user2", qId);
        TestHelper.addAnAnswer(mockMvc, "q", "a", "user3", qId);
        
        MvcResult result = mockMvc.perform(get("/quiz/1/answer")).andReturn();
        JSONArray array = new JSONArray(result.getResponse().getContentAsString());
        assertEquals(3, array.length());
        
        result = mockMvc.perform(get("/answer")).andReturn();
        array = new JSONArray(result.getResponse().getContentAsString());
        assertEquals(6, array.length());
    }
    
    @Test
    @DirtiesContext
    public void cannotDeleteWithBadAnswerQuizCombination() throws Exception {
        TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true, 2);
        TestHelper.addQuizWithOneQuestion(mockMvc, "quiz2", "question1", true, 2);
       
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", 1L);
        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user2", 2L);
        
        TestHelper.addAReview(mockMvc, 1L, 1L, "reviewer_guy", "good job!");
        TestHelper.addAReview(mockMvc, 2L, 2L, "reviewer_guy", "good job!");
        
        mockMvc.perform(delete("/quiz/1/answer/2/review")).andExpect(status().is4xxClientError());
    }
}