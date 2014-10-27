package app.controllers;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
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
    
    public static Long addAnAnswer(MockMvc mockMvc, String question, String answer, String user, Long quizId) throws Exception {
        String jsonAnswer = 
                "{\"answer\":\"[{"
                + "\\\"question\\\":\\\"" + question + "\\\","
                + "\\\"value\\\":\\\"" + answer + "\\\"}]\","
                + "\"user\":\"" + user + "\"}";
        
        String url = "/quiz/" + quizId + "/answer";
        
        MockHttpServletResponse response = 
                mockMvc.perform(post(url).content(jsonAnswer).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        
        JsonObject jo = TestHelper.getObjectByKeyFromJson(response.getContentAsString(), "answer");
        
        return jo.get("id").getAsLong();
    }
    
    public static String addAnswerAndGetUserhash(MockMvc mockMvc, String question, String answer, String user, Long quizId) throws Exception {
        String jsonAnswer = 
                "{\"answer\":\"[{"
                + "\\\"question\\\":\\\"" + question + "\\\","
                + "\\\"value\\\":\\\"" + answer + "\\\"}]\","
                + "\"user\":\"" + user + "\"}";
        
        String url = "/quiz/" + quizId + "/answer";
        
        MockHttpServletResponse response;
        response = mockMvc.perform(post(url).content(jsonAnswer).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        
        JsonParser jp = new JsonParser();
        JsonElement e = jp.parse(response.getContentAsString());
        
        String userhash = e.getAsJsonObject().get("userhash").getAsString();
        
        return userhash;
    }
    
    public static void addAReview(MockMvc mockMvc, Long quizId, Long answerId, String reviewer, String review) throws Exception {
        String jsonReview = 
                "{\"reviewer\":\"" + reviewer + "\"," +
                "\"review\":\"" + review + "\"}";
        
        String url = "/quiz/" + quizId + "/answer/"+ answerId +"/review";
        
        mockMvc.perform(post(url).content(jsonReview).contentType(MediaType.APPLICATION_JSON));
    }
    
    public static String getStringByKeyAndIndexFromJsonArray(String json, String key, Integer i) {
        JsonParser jp = new JsonParser();
        JsonArray ja = jp.parse(json).getAsJsonArray();
        JsonObject jo = ja.get(i).getAsJsonObject();
        if(jo.get(key).isJsonNull()) return null;
        return jo.get(key).getAsString();
    }
    
    public static Integer getIntegerByKeyAndIndexFromJsonArray(String json, String key, Integer i) {
        JsonParser jp = new JsonParser();
        JsonArray ja = jp.parse(json).getAsJsonArray();
        JsonObject jo = ja.get(i).getAsJsonObject();
        Integer val = jo.get(key).getAsInt();
        if(jo.get(key).isJsonNull()) return null;
        return val;
    }
    
    public static Long getLongByKeyAndIndexFromJsonArray(String json, String key, Integer i) {
        JsonParser jp = new JsonParser();
        JsonArray ja = jp.parse(json).getAsJsonArray();
        JsonObject jo = ja.get(i).getAsJsonObject();
        Long val = jo.get(key).getAsLong();
        if(jo.get(key).isJsonNull()) return null;
        return val;
    }
    
    public static String getStringByKeyFromJson(String json, String key) {
        JsonParser jp = new JsonParser();
        JsonObject jo = jp.parse(json).getAsJsonObject();
        if(jo.get(key).isJsonNull()) return null;
        return jo.get(key).getAsString();
    }
    
    public static Integer getIntegerByKeyFromJson(String json, String key) {
        JsonParser jp = new JsonParser();
        JsonObject jo = jp.parse(json).getAsJsonObject();
        if(jo.get(key).isJsonNull()) return null;
        return jo.get(key).getAsInt();
    }
    
    public static JsonObject getObjectByKeyFromJson(String json, String key) {
        JsonParser jp = new JsonParser();
        JsonObject jo = jp.parse(json).getAsJsonObject();
        if(!jo.get(key).isJsonObject()) return null;
        return jo.get(key).getAsJsonObject();
    }

    static Long getLongByKeyFromJson(String json, String key) {
        JsonParser jp = new JsonParser();
        JsonObject jo = jp.parse(json).getAsJsonObject();
        if(jo.get(key).isJsonNull()) return null;
        return jo.get(key).getAsLong();
    }
}
