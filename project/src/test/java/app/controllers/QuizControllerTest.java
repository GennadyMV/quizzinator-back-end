package app.controllers;


import app.Application;
import app.domain.Quiz;
import app.repositories.QuizRepository;
import java.util.List;
import org.hibernate.Hibernate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class QuizControllerTest {
    
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testEmptyQuizList() throws Exception {
        this.mockMvc.perform(get("/quiz"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
    
    @Test
    public void testAddingQuiz() throws Exception {
        String jsonQuiz = "{\"title\":\"testquiz1\",\"items\":\"["
                + "{}]\"}";
        
        this.mockMvc.perform(post("/quiz").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
        
        List<Quiz> quizes = quizRepository.findAll();
        for (int i = 0; i < quizes.size(); i++) {
            if (quizes.get(i).getTitle().equals("testquiz1")) {
                return;
            }
        }
        
        throw new AssertionError();
    }
    
    @Test
    public void testCorrectOpenQuestionsAdded() throws Exception {
        String jsonQuiz = "{\"title\":\"testquiz2\",\"items\":\"["
                + "{\\\"question\\\":\\\"testquestion1\\\",\\\"item_type\\\":\\\"open_question\\\",\\\"$$hashKey\\\":\\\"003\\\"},"
                + "{\\\"question\\\":\\\"testquestion2\\\",\\\"item_type\\\":\\\"open_question\\\",\\\"$$hashKey\\\":\\\"006\\\"}"
            + "]\"}";
        
        this.mockMvc.perform(post("/quiz").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
        
        List<Quiz> quizes = quizRepository.findAll();
        JSONArray items = new JSONArray(quizes.get(quizes.size()-1).getItems());
        
        Assert.assertEquals("testquestion1", items.getJSONObject(0).getString("question"));
        Assert.assertEquals("open_question", items.getJSONObject(0).getString("item_type"));
        Assert.assertEquals("testquestion2", items.getJSONObject(1).getString("question"));
        Assert.assertEquals("open_question", items.getJSONObject(1).getString("item_type"));
    }
    
    @Test
    public void testCorrectNumberOfOpenQuestionsAdded() throws Exception {
        String jsonQuiz = "{\"title\":\"testquiz3\",\"items\":\"["
                + "{\\\"question\\\":\\\"testquestion1\\\",\\\"item_type\\\":\\\"open_question\\\"},"
                + "{\\\"question\\\":\\\"testquestion2\\\",\\\"item_type\\\":\\\"open_question\\\"},"
                + "{\\\"question\\\":\\\"testquestion3\\\",\\\"item_type\\\":\\\"open_question\\\"}"
                + "]\"}";
        
        this.mockMvc.perform(post("/quiz").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
        
        List<Quiz> quizes = quizRepository.findAll();
        JSONArray items = new JSONArray(quizes.get(quizes.size()-1).getItems());
        
        Assert.assertEquals(3, items.length());
    }
}