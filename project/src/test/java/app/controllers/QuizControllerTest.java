package app.controllers;


import app.Application;
import app.domain.Quiz;
import app.repositories.QuizRepository;
import java.util.List;
import org.hibernate.Hibernate;
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
    public void testPostingJsonQuiz() throws Exception {
        String jsonQuiz = "{\"title\": \"kysymys 1\", \"openQuestions\": []}";
        
        this.mockMvc.perform(post("/quiz").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/quiz/*"));
    }
    
    /**
    @Test
    public void addingToDatabase() throws Exception {
        String jsonQuiz = "{\"title\": \"testquiz 1\", \"openQuestions\": []}";
        
        this.mockMvc.perform(post("/quiz").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
        
        List<Quiz> quizes = quizRepository.findAll();
        for (int i = 0; i < quizes.size(); i++) {
            if (quizes.get(i).getTitle().equals("testquiz 1")) {
                return;
            }
        }
        
        throw new AssertionError();
    }
    
    
    @Test
    public void correctOpenQuestionsAdded() throws Exception {
        String jsonQuiz = "{\"title\": \"testquiz 2\","
                    + "\"openQuestions\": ["
                        + "{\"question\": \"testquestion 1\", \"itemOrder\": 0},"
                        + "{\"question\": \"testquestion 2\", \"itemOrder\": 1}"
                    + "]}";
        
        this.mockMvc.perform(post("/quiz").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
        
        List<Quiz> quizes = quizRepository.findAll();
        List<OpenQuestion> openQuestions = quizes.get(quizes.size()-1).getOpenQuestions();
        
        Assert.assertEquals("testquestion 1", openQuestions.get(0).getQuestion());
        Assert.assertEquals("testquestion 2", openQuestions.get(1).getQuestion());
    }
    
    @Test
    public void correctNumberOfOpenQuestionsAdded() throws Exception {
        String jsonQuiz = "{\"title\": \"testquiz 3\","
                    + "\"openQuestions\": ["
                        + "{\"question\": \"testquestion 3\", \"itemOrder\": 0},"
                        + "{\"question\": \"testquestion 4\", \"itemOrder\": 1},"
                        + "{\"question\": \"testquestion 5\", \"itemOrder\": 2}"
                    + "]}";
        
        this.mockMvc.perform(post("/quiz").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
        
        List<Quiz> quizes = quizRepository.findAll();
        List<OpenQuestion> openQuestions = quizes.get(quizes.size()-1).getOpenQuestions();
        
        Assert.assertEquals(3, openQuestions.size());
    }
    * */
}