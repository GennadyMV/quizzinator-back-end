package app.controllers;


import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class TestHelper {
    public static Long addQuizWithOneQuestion(MockMvc mockMvc, 
            String quizTitle, 
            String question, 
            boolean reviewable, 
            int reviewRounds) throws Exception {
        
        String jsonQuiz = 
                "{\"title\":\"" + quizTitle + "\"," + 
                "\"reviewable\":" + Boolean.toString(reviewable) + "," + 
                "\"reviewRounds\":" + Integer.toString(reviewRounds) + "," + 
                "\"items\":\"[{\\\"question\\\":\\\"" + question + "\\\"," + 
                "\\\"item_type\\\":\\\"open_question\\\"}]\"}";
        
        String url = "/quiz";
        
        String redirectUrl = mockMvc.perform(
                post(url).content(jsonQuiz).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getRedirectedUrl();
        return Long.parseLong(redirectUrl.split("/")[2]);
    }
    
    public static Long addQuizWithOneQuestion(MockMvc mockMvc, String quizTitle, String question, boolean reviewable) throws Exception {
        return TestHelper.addQuizWithOneQuestion(mockMvc, quizTitle, question, reviewable, 1);
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
        
        String url = "/quiz/" + quizId + "/answer/"+ answerId +"/review";
        
        mockMvc.perform(post(url).content(jsonReview).contentType(MediaType.APPLICATION_JSON));
    }
}
