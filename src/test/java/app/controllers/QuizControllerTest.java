package app.controllers;

import app.Application;
import app.domain.Quiz;
import app.domain.QuizAnswer;
import app.repositories.QuizAnswerRepository;
import app.repositories.QuizRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    private QuizAnswerRepository answerRepository;

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
    @DirtiesContext
    public void testAddingQuiz() throws Exception {
        postTestquiz1();
        assertTrue(quizRepository.findOne(1L).getTitle().equals("testquiz1"));
    }

    @Test
    @DirtiesContext
    public void testCorrectOpenQuestionsAdded() throws Exception {
        postTestquiz2();

        List<Quiz> quizes = quizRepository.findAll();
        JSONArray items = new JSONArray(quizes.get(quizes.size() - 1).getItems());

        assertEquals("testquestion1", items.getJSONObject(0).getString("question"));
        assertEquals("open_question", items.getJSONObject(0).getString("item_type"));
        assertEquals("testquestion2", items.getJSONObject(1).getString("question"));
        assertEquals("open_question", items.getJSONObject(1).getString("item_type"));
    }

    @Test
    @DirtiesContext
    public void testCorrectNumberOfOpenQuestionsAdded() throws Exception {
        postTestquiz3();

        List<Quiz> quizes = quizRepository.findAll();
        JSONArray items = new JSONArray(quizes.get(quizes.size() - 1).getItems());

        assertEquals(3, items.length());
    }
    @Test
    @DirtiesContext
    public void testIsOpenSetCorrectly() throws Exception {
        postTestquiz1();
        
        assertTrue(quizRepository.findOne(1L).getIsOpen());
    }

    @Test
    @DirtiesContext
    public void testIsOpenReturnedCorrectly() throws Exception {
        Quiz quiz = new Quiz();
        quiz.setIsOpen(false);
        quiz.setTitle("testquiz");
        quiz.setItems("[{}]");
        quizRepository.save(quiz);
        
        MvcResult mvcResult = this.mockMvc.perform(get("/quiz/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        
        Gson gson = new Gson();
        Quiz returnedQuiz = gson.fromJson(mvcResult.getResponse().getContentAsString(), Quiz.class);
        
        assertFalse(returnedQuiz.getIsOpen());
    }
    
    @Test
    @DirtiesContext
    public void testCorrectPlaceholderAnswerAdded() throws Exception {
        postTestquiz1();
        
        String jsonAnswer = "{\"answer\": \"vastaus\"}";
        this.mockMvc.perform(post("/quiz/1/placeholder").content(jsonAnswer).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());
        
        Quiz q = quizRepository.findOne(1L);
        List<QuizAnswer> answers = answerRepository.findByQuizAndPlaceholderIsTrue(q);
        JSONObject json = new JSONObject(answers.get(0).getAnswer());
        String answer = json.getString("answer");
        assertTrue(answer.equals("vastaus"));
    }

    @Test
    @DirtiesContext
    public void testMultiplePlaceholderAnswersAdded() throws Exception {
        postTestquiz1();

        String jsonAnswer = "{\"answer\": \"vastaus\"}";
        this.mockMvc.perform(post("/quiz/1/placeholder").content(jsonAnswer).contentType(MediaType.APPLICATION_JSON));
        jsonAnswer = "{\"answer\": \"vastaus\"}";
        this.mockMvc.perform(post("/quiz/1/placeholder").content(jsonAnswer).contentType(MediaType.APPLICATION_JSON));
        jsonAnswer = "{\"answer\": \"vastaus\"}";
        this.mockMvc.perform(post("/quiz/1/placeholder").content(jsonAnswer).contentType(MediaType.APPLICATION_JSON));

        Quiz q = quizRepository.findOne(1L);
        List<QuizAnswer> answers = answerRepository.findByQuizAndPlaceholderIsTrue(q);
        assertEquals(3, answers.size());
    }

    @Test
    @DirtiesContext
    public void testAddingItemsToExistingQuiz() throws Exception {
        postTestquiz1();

        String editedQuiz = "{\"title\":\"testquiz2\",\"items\":\"["
                + "{\\\"question\\\":\\\"testquestion1\\\",\\\"item_type\\\":\\\"open_question\\\",\\\"$$hashKey\\\":\\\"003\\\"},"
                + "{\\\"question\\\":\\\"testquestion2\\\",\\\"item_type\\\":\\\"open_question\\\",\\\"$$hashKey\\\":\\\"006\\\"}"
                + "]\"}";
        this.mockMvc.perform(post("/quiz/1").content(editedQuiz).contentType(MediaType.APPLICATION_JSON));

        List<Quiz> quizes = quizRepository.findAll();
        JSONArray items = new JSONArray(quizes.get(quizes.size() - 1).getItems());

        assertEquals(2, items.length());
    }

    @Test
    @DirtiesContext
    public void testRemovingItemsFromExistingQuiz() throws Exception {
        postTestquiz2();

        String editedQuiz = "{\"title\":\"testquiz2\",\"items\":\"["
                + "{\\\"question\\\":\\\"testquestion1\\\",\\\"item_type\\\":\\\"open_question\\\",\\\"$$hashKey\\\":\\\"003\\\"},"
                + "]\"}";

        this.mockMvc.perform(post("/quiz/1").content(editedQuiz).contentType(MediaType.APPLICATION_JSON));

        List<Quiz> quizes = quizRepository.findAll();
        JSONArray items = new JSONArray(quizes.get(quizes.size() - 1).getItems());

        assertEquals(1, items.length());
    }
    
    @Test
    @DirtiesContext
    public void numberOfQuizesIncreasedByOneAfterClone() throws Exception {
        postTestquiz1();
        
        long count = quizRepository.count();
        
        this.mockMvc.perform(post("/quiz/"+count+"/clone").contentType(MediaType.APPLICATION_JSON));
        
        assertEquals(count+1, quizRepository.count());
    }
    
    @Test
    @DirtiesContext
    public void cloneInfoMatchesOriginal() throws Exception {
        postTestquiz1();
        postTestquiz3();
        postTestquiz2();
        
        this.mockMvc.perform(post("/quiz/2/clone").contentType(MediaType.APPLICATION_JSON));
        
        Quiz quiz = quizRepository.findOne(2L);
        Quiz cloneQuiz = quizRepository.findOne(4L);
        
        assertEquals(quiz.getIsOpen(), cloneQuiz.getIsOpen());
        assertTrue(quiz.getItems().equals(cloneQuiz.getItems()));
        assertEquals(quiz.isReviewable(), cloneQuiz.isReviewable());
        assertTrue(quiz.getTitle().equals(cloneQuiz.getTitle()));
    }
    
    @Test
    @DirtiesContext
    public void testAnsweredFieldIsTrueAfterAnswering() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
        TestHelper.addAnAnswer(mockMvc, "question1", "asnwer", "user1", quizId);
        
        MvcResult mvcAnswer = this.mockMvc.perform(get("/quiz/" + quizId)
                            .param("username", "user1")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andReturn();
        
        JsonParser jsonParser = new JsonParser();
        JsonElement o = jsonParser.parse(mvcAnswer.getResponse().getContentAsString());
        Boolean answered = o.getAsJsonObject().getAsJsonPrimitive("answered").getAsBoolean();
        
        assertTrue(answered);
    }

    @Test
    @DirtiesContext
    public void testAnsweredFieldIsFalseBeforeAnswering() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
        
        MvcResult mvcAnswer = this.mockMvc.perform(get("/quiz/" + quizId + "?username=user1")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andReturn();
        
        JsonParser jsonParser = new JsonParser();
        JsonElement o = jsonParser.parse(mvcAnswer.getResponse().getContentAsString());
        Boolean answered = o.getAsJsonObject().getAsJsonPrimitive("answered").getAsBoolean();
        
        assertFalse(answered);
    }

    @Test
    @DirtiesContext
    public void testAnsweredFieldIsFalseAfterSomeoneElseHasAnsweredButBeforeIHaveAnswered() throws Exception {
        Long quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
        TestHelper.addAnAnswer(mockMvc, "question1", "asnwer", "user2", quizId);
        
        MvcResult mvcAnswer = this.mockMvc.perform(get("/quiz/" + quizId + "?username=user1")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andReturn();
        
        JsonParser jsonParser = new JsonParser();
        JsonElement o = jsonParser.parse(mvcAnswer.getResponse().getContentAsString());
        Boolean answered = o.getAsJsonObject().getAsJsonPrimitive("answered").getAsBoolean();
        
        assertFalse(answered);
    }
    
    @Test
    @DirtiesContext
    public void quizNotExpiredWhenDeadlineNotSet() throws ParseException {
        Quiz quiz = new Quiz();
        quiz.setIsOpen(false);
        quiz.setTitle("testquiz");
        quiz.setItems("[{}]");
        quizRepository.save(quiz);
        
        assertFalse(quiz.isAnsweringExpired());
        assertFalse(quiz.isReviewingExpired());
    }
    
    @Test
    @DirtiesContext
    public void deadlineNotExpiredReturnedCorrectly() throws ParseException {
        Quiz quiz = new Quiz();
        quiz.setIsOpen(false);
        quiz.setTitle("testquiz");
        quiz.setItems("[{}]");
        
        quiz.setAnswerDeadline(new SimpleDateFormat("MM-dd-yyyy").parse("03-11-2025"));
        quiz.setAnswerDeadline(new SimpleDateFormat("MM-dd-yyyy").parse("02-1-2037"));
        quizRepository.save(quiz);
        
        assertFalse(quiz.isAnsweringExpired());
        assertFalse(quiz.isReviewingExpired());
    }
    
    @Test
    @DirtiesContext
    public void deadlineExpiredReturnedCorrectly() throws ParseException {
        Quiz quiz = new Quiz();
        quiz.setIsOpen(false);
        quiz.setTitle("testquiz");
        quiz.setItems("[{}]");
        quiz.setAnswerDeadline(new SimpleDateFormat("MM-dd-yyyy").parse("08/06/2014"));
        quiz.setAnswerDeadline(new SimpleDateFormat("MM-dd-yyyy").parse("10/22/2014"));
        quizRepository.save(quiz);
        
        assertTrue(quiz.isAnsweringExpired());
        assertTrue(quiz.isReviewingExpired());
    }
    
    @Test
    @DirtiesContext
    public void testGetPlaceholderAnswers() throws Exception {
        postTestquiz1();

        String jsonAnswer = "{\"answer\": \"vastaus\"}";
        this.mockMvc.perform(post("/quiz/1/placeholder").content(jsonAnswer).contentType(MediaType.APPLICATION_JSON));
        jsonAnswer = "{\"answer\": \"vastaus\"}";
        this.mockMvc.perform(post("/quiz/1/placeholder").content(jsonAnswer).contentType(MediaType.APPLICATION_JSON));
        jsonAnswer = "{\"answer\": \"vastaus\"}";
        this.mockMvc.perform(post("/quiz/1/placeholder").content(jsonAnswer).contentType(MediaType.APPLICATION_JSON));

        MvcResult result = this.mockMvc.perform(get("/quiz/1/placeholder")).andReturn();
        JSONArray answers = new JSONArray(result.getResponse().getContentAsString());
        assertEquals(3, answers.length());
    }
    
    @Test
    @DirtiesContext
    public void testGetQuizReturnsUsersLatestAnswer() throws Exception {
        TestHelper.addQuizWithOneQuestion(mockMvc, "quiz", "question1", true);
        MvcResult mvcResult = this.mockMvc.perform(get("/quiz/1")
                .contentType(MediaType.APPLICATION_JSON)
                .param("username", "masa"))
                .andReturn();
        
        JSONObject returnedQuiz = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertFalse(returnedQuiz.getBoolean("answered"));
        assertTrue(returnedQuiz.getString("myLatestAnswer").equals("null"));
        
        TestHelper.addAnAnswer(mockMvc, "question1", "moi", "masa", 1L);
        
        mvcResult = this.mockMvc.perform(get("/quiz/1")
                .contentType(MediaType.APPLICATION_JSON)
                .param("username", "masa"))
                .andReturn();
        
        returnedQuiz = new JSONObject(mvcResult.getResponse().getContentAsString());
        String answer = new JSONObject(
                new JSONArray(
                new JSONObject(returnedQuiz.getString("myLatestAnswer"))
                .getString("answer")).getString(0)).getString("value");
        
        assertTrue(returnedQuiz.getBoolean("answered"));
        assertTrue(answer.equals("moi"));
        
        TestHelper.addAnAnswer(mockMvc, "question1", "sup", "masa", 1L);
        
        mvcResult = this.mockMvc.perform(get("/quiz/1")
                .contentType(MediaType.APPLICATION_JSON)
                .param("username", "masa"))
                .andReturn();
        
        returnedQuiz = new JSONObject(mvcResult.getResponse().getContentAsString());
        answer = new JSONObject(
                new JSONArray(
                new JSONObject(returnedQuiz.getString("myLatestAnswer"))
                .getString("answer")).getString(0)).getString("value");
        
        assertTrue(returnedQuiz.getBoolean("answered"));
        assertTrue(answer.equals("sup"));
        
        TestHelper.addAnAnswer(mockMvc, "question1", "hehe", "masa", 1L);
        
        mvcResult = this.mockMvc.perform(get("/quiz/1")
                .contentType(MediaType.APPLICATION_JSON)
                .param("username", "masa"))
                .andReturn();
        
        System.out.println("|||||||||||||||");
        System.out.println(mvcResult.getResponse().getContentAsString());
        System.out.println("|||||||||||||||");
        
        returnedQuiz = new JSONObject(mvcResult.getResponse().getContentAsString());
        answer = new JSONObject(
                new JSONArray(
                new JSONObject(returnedQuiz.getString("myLatestAnswer"))
                .getString("answer")).getString(0)).getString("value");
        
        assertTrue(returnedQuiz.getBoolean("answered"));
        assertTrue(answer.equals("hehe"));
    }
    
    private void postTestquiz1() throws Exception {
        String jsonQuiz = "{\"title\":\"testquiz1\",\"isOpen\":\"true\",\"items\":\"["
                + "{}]\"}";
        this.mockMvc.perform(post("/quiz").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
    }
    
    private void postTestquiz2() throws Exception {
        String jsonQuiz = "{\"title\":\"testquiz2\",\"items\":\"["
                + "{\\\"question\\\":\\\"testquestion1\\\",\\\"item_type\\\":\\\"open_question\\\",\\\"$$hashKey\\\":\\\"003\\\"},"
                + "{\\\"question\\\":\\\"testquestion2\\\",\\\"item_type\\\":\\\"open_question\\\",\\\"$$hashKey\\\":\\\"006\\\"}"
                + "]\"}";

        this.mockMvc.perform(post("/quiz").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
    }
    
    private void postTestquiz3() throws Exception {
        String jsonQuiz = "{\"title\":\"testquiz3\",\"items\":\"["
                + "{\\\"question\\\":\\\"testquestion1\\\",\\\"item_type\\\":\\\"open_question\\\"},"
                + "{\\\"question\\\":\\\"testquestion2\\\",\\\"item_type\\\":\\\"open_question\\\"},"
                + "{\\\"question\\\":\\\"testquestion3\\\",\\\"item_type\\\":\\\"open_question\\\"}]\","
                + "\"isOpen\":\"true\",\"reviewable\":\"true\"}";
        this.mockMvc.perform(post("/quiz").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
    }
}