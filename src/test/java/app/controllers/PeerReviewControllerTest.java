package app.controllers;

import app.Application;
import app.domain.PeerReview;
import com.google.gson.Gson;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    
//    @Test
//    @DirtiesContext
//    public void addingPeerReviewWorks() throws Exception {
////        Long quizId, answerId;
//        TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
//        String jsonQuiz = "{\"title\":\"testquiz2\",\"reviewable\":\"true\",\"items\":\"["
//                + "{\\\"question\\\":\\\"testquestion1\\\",\\\"item_type\\\":\\\"open_question\\\",\\\"$$hashKey\\\":\\\"003\\\"}"
//                + "]\"}";
//
//        this.mockMvc.perform(post("/quiz").content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
//        
//        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", 1L);
//        
//        //horrible things happen here to get the real answer id. i think the response should be redirect to answer, not the reviewable answers
////        MockHttpServletResponse response = mockMvc.perform(get("/quiz/" + quizId + "/answer")).andReturn().getResponse();
////        String content = response.getContentAsString();
////        
////        Pattern p = Pattern.compile("\"id\":(\\d+),");
////        Matcher m = p.matcher(content);
////        if (m.find()) {
////            answerId = Long.parseLong(m.group(1));
////        } else {
////            assertTrue(false);
////            return;
////        }
//        
//        TestHelper.addAReview(mockMvc, 1L, 1L, "user2", "good answer!");
//        
//        MvcResult mvcAnswer = this.mockMvc.perform(get("/quiz/1/answer/1/review"))
//            .andExpect(status().isOk())
//            .andReturn();
//        
//        Gson gson = new Gson();
//        List<PeerReview> reviews = gson.fromJson(mvcAnswer.getResponse().getContentAsString(), List.class);
//        
//        assertTrue(reviews.get(0).getReviewer().getName().equals("user2"));
//        assertTrue(reviews.get(0).getReview().equals("good answer!"));
//    }
//    
//    @Test
//    @DirtiesContext
//    public void leastReviewedAnswersAreOffered() throws Exception {
//        Long quizId, answerId, reviewId;
//        
//        quizId = TestHelper.addQuizWithOneQuestion(mockMvc, "quiz1", "question1", true);
//        TestHelper.addAnAnswer(mockMvc, "question1", "answer1", "user1", quizId);
//        
//        //same as with addingPeerReviewWorks
//        MockHttpServletResponse response = mockMvc.perform(get("/quiz/" + quizId + "/answer")).andReturn().getResponse();
//        String content = response.getContentAsString();
//        
//        Pattern p = Pattern.compile("\"id\":(\\d+),");
//        Matcher m = p.matcher(content);
//        if (m.find()) {
//            answerId = Long.parseLong(m.group(1));
//        } else {
//            assertTrue(false);
//            return;
//        }
//        
//        TestHelper.addAnAnswer(mockMvc, "question1", "answer2", "user2", quizId);
//        TestHelper.addAnAnswer(mockMvc, "question1", "answer3", "user3", quizId);
//        
//        TestHelper.addAReview(mockMvc, quizId, answerId, "user2", "good answer1!");
//        TestHelper.addAReview(mockMvc, quizId, answerId, "user2", "good answer2!");
//        TestHelper.addAReview(mockMvc, quizId, answerId, "user2", "good answer3!");
//        TestHelper.addAReview(mockMvc, quizId, answerId, "user2", "good answer4!");
//        
//        
//        String jsonAnswer = 
//                "{\"answer\":\"[{"
//                + "\\\"question\\\":\\\"question1\\\","
//                + "\\\"value\\\":\\\"answer4\\\"}]\","
//                + "\"user\":\"user4\"}";
//        
//        response = mockMvc.perform(post("/quiz/" + quizId + "/answer").content(jsonAnswer).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn().getResponse();
//        
//        content = response.getContentAsString();
//        
//        assertTrue(content.contains("\"id\":2"));
//        assertTrue(content.contains("\"id\":3"));
//    }
}
