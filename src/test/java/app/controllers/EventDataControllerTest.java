package app.controllers;

import app.Application;
import app.domain.EventData;
import app.repositories.EventDataRepository;
import app.repositories.QuizRepository;
import app.repositories.UserRepository;
import java.sql.Timestamp;
import javax.transaction.Transactional;
import org.json.JSONArray;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.Assert.*;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class EventDataControllerTest {
    
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    
    @Autowired
    private QuizRepository quizRepo;
    
    @Autowired
    private EventDataRepository eventRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    @Test
    @DirtiesContext
    @Transactional
    public void testAddEventDataExistingUser() throws Exception {
        TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
        TestHelper.addAnAnswer(mockMvc, "question1", "heh", "pertti", 1L);
        
        assertEquals(1, quizRepo.count());
        assertEquals(1, userRepo.count());
        
        Long millis = 1415716083277L;
        postEventData(1L, "pertti", millis);
        
        assertEquals(1, eventRepo.count());
        
        EventData eventData = eventRepo.findOne(1L);
        assertTrue(eventData.getUser().getName().equals("pertti"));
        
        assertTrue(eventData.getAction().equals("press"));
        assertTrue(eventData.getElement().equals("button"));
        assertTrue(eventData.getValue().equals("success"));
        assertTrue(eventData.getChildElement().equals("child1"));
        assertEquals(new Timestamp(millis), eventData.getActionTime());
    }
    
    @Test
    @DirtiesContext
    @Transactional
    public void testAddClickDataNonExistentUser() throws Exception {
        TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
        
        assertEquals(1, quizRepo.count());
        assertEquals(0, userRepo.count());
        
        Long millis = 1415716083277L;
        postEventData(1L, "pertti", millis);
        
        assertEquals(1, userRepo.count());
        assertEquals(1, eventRepo.count());
        
        EventData eventData = eventRepo.findOne(1L);
        assertTrue(eventData.getUser().getName().equals("pertti"));
        
        assertTrue(eventData.getAction().equals("press"));
        assertTrue(eventData.getElement().equals("button"));
        assertTrue(eventData.getValue().equals("success"));
        assertTrue(eventData.getChildElement().equals("child1"));
        assertEquals(new Timestamp(millis), eventData.getActionTime());
    }
    
    @Test
    @DirtiesContext
    @Transactional
    public void testGetUserClickData() throws Exception {
        TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
        
        Long millis = 1415716083388L;
        postEventData(1L, "pertti", millis);
        postEventData(1L, "pertti", millis);
        postEventData(1L, "masa", millis);
        
        MvcResult result = mockMvc.perform(get("/events/user/pertti"))
                .andExpect(status().isOk())
                .andReturn();
        
        JSONArray clicks = new JSONArray(result.getResponse().getContentAsString());
        
        assertEquals(2, clicks.length());
        assertTrue(clicks.getJSONObject(0).getString("user").equals("pertti"));
        assertTrue(clicks.getJSONObject(1).getString("user").equals("pertti"));
    }
    
    @Test
    @DirtiesContext
    @Transactional
    public void testGetQuizClickData() throws Exception {
        TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
        TestHelper.addQuizWithOneQuestion(mockMvc, "quiz2", "question1", true);
        
        Long millis = 1415716083388L;
        postEventData(1L, "pertti", millis);
        postEventData(1L, "masa", millis);
        postEventData(2L, "masa", millis);
        
        MvcResult result = mockMvc.perform(get("/events/quiz/2"))
                .andExpect(status().isOk())
                .andReturn();
        
        JSONArray clicks = new JSONArray(result.getResponse().getContentAsString());
        
        assertEquals(1, clicks.length());
        assertTrue(clicks.getJSONObject(0).getString("user").equals("masa"));
        
        assertEquals(2L, clicks.getJSONObject(0).getLong("quizId"));
    }
    
    @Test
    @DirtiesContext
    public void cannotGetClicksForNonexistentQuiz() throws Exception {
        TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
        
        Long millis = 1415716083388L;
        postEventData(1L, "pertti", millis);
        postEventData(1L, "masa", millis);
        
        mockMvc.perform(get("/events/quiz/2")).andExpect(status().is4xxClientError());
    }
    
    @Test
    @DirtiesContext
    public void cannotGetClicksForNonexistentUser() throws Exception {
        TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
        
        Long millis = 1415716083388L;
        postEventData(1L, "pertti", millis);
        postEventData(1L, "masa", millis);
        
        mockMvc.perform(get("/events/quiz/erno")).andExpect(status().is4xxClientError());
    }
    
    private void postEventData(Long quizId, String username, Long actionTime) throws Exception {
        String jsonEvent = "{\"quizId\":"+quizId+","
                + "\"user\":\""+username+"\","
                + "\"events\":["
                    + "{\"actionTime\":"+actionTime+","
                    + "\"element\":\"button\","
                    + "\"action\":\"press\","
                    + "\"value\":\"success\","
                    + "\"child\":\"child1\"}]}";
        
        mockMvc.perform(post("/events").content(jsonEvent).contentType(MediaType.APPLICATION_JSON));
    }
}