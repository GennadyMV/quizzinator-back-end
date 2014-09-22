package app.controllers;


import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class TestHelper {
    public static void addQuizWithOneQuestion(MockMvc mockMvc, String quizTitle, String question, boolean reviewable) throws Exception {
        String jsonQuiz = 
                "{\"title\":\"" + quizTitle + "\"," + 
                "\"reviewable\":" + Boolean.toString(reviewable) + "," + 
                "\"items\":\"[{\\\"question\\\":\\\"" + question + "\\\"," + 
                "\\\"item_type\\\":\\\"open_question\\\"}]\"}";
        
        String url = "/quiz";
        
        mockMvc.perform(post(url).content(jsonQuiz).contentType(MediaType.APPLICATION_JSON));
    }
    
    public static void addAnAnswer(MockMvc mockMvc, String question, String answer, String user, Long quizId) throws Exception {
        String jsonAnswer = 
                "{\"answer\":\"[{"
                + "\\\"question\\\":\\\"" + question + "\\\","
                + "\\\"value\\\":\\\"" + answer + "\\\"}]\","
                + "\"user\":\"" + user + "\"}";
        
        String url = "/quiz/" + quizId + "/answer";
        
        mockMvc.perform(post(url).content(jsonAnswer).contentType(MediaType.APPLICATION_JSON));
    }
    
    public static void addAReview(MockMvc mockMvc, Long quizId, Long answerId, String reviewer, String review) throws Exception {
        String jsonReview = 
                "{\"reviewer\":\"" + reviewer + "\"," +
                "\"review\":\"" + review + "\"}";
        
        String url = "/quiz/" + quizId + "/answer/" + answerId + "/review";
        
        mockMvc.perform(post(url).content(jsonReview).contentType(MediaType.APPLICATION_JSON));
    }
}
