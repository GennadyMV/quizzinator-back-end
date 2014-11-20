package app.domain;

import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class QuizTest {
    private Quiz quiz;
    private Long oneDayInMilliseconds;
    private Long oneWeekInMilliseconds;
    
    private Date now;
    private Date past;
    private Date future;
    
    public QuizTest() {
        oneDayInMilliseconds = 1000L*60*60*24;
        oneWeekInMilliseconds = oneDayInMilliseconds*7;
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        quiz = new Quiz();
        now = new Date();
        past = new Date(now.getTime() - oneWeekInMilliseconds);
        future = new Date(now.getTime() + oneWeekInMilliseconds);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testAnsweringExpired() {
        quiz.setAnswerDeadline(past);
        assertTrue(quiz.answeringExpired());
        
        quiz.setAnswerDeadline(future);
        assertFalse(quiz.answeringExpired());
        
        quiz.setAnswerDeadline(null);
        assertFalse(quiz.answeringExpired());
    }

    @Test
    public void testReviewingExpired() {
        quiz.setReviewDeadline(past);
        assertTrue(quiz.reviewingExpired());
        
        quiz.setReviewDeadline(future);
        assertFalse(quiz.reviewingExpired());
        
        quiz.setReviewDeadline(null);
        assertFalse(quiz.reviewingExpired());
    }
    
    @Test
    public void testImprovingPossible() {
        quiz.setAnswerImproveStart(future);
        quiz.setAnswerImproveDeadline(future);
        assertFalse(quiz.improvingPossible());
        
        quiz.setAnswerImproveStart(past);
        quiz.setAnswerImproveDeadline(past);
        assertFalse(quiz.improvingPossible());
        
        quiz.setAnswerImproveStart(future);
        quiz.setAnswerImproveDeadline(past);
        assertFalse(quiz.improvingPossible());
        
        quiz.setAnswerImproveStart(future);
        quiz.setAnswerImproveDeadline(past);
        assertFalse(quiz.improvingPossible());
        
        quiz.setAnswerImproveStart(past);
        quiz.setAnswerImproveDeadline(future);
        assertTrue(quiz.improvingPossible());
        
        quiz.setAnswerImproveStart(null);
        quiz.setAnswerImproveDeadline(future);
        assertTrue(quiz.improvingPossible());
        
        quiz.setAnswerImproveStart(past);
        quiz.setAnswerImproveDeadline(null);
        assertTrue(quiz.improvingPossible());
        
        quiz.setAnswerImproveStart(null);
        quiz.setAnswerImproveDeadline(null);
        assertTrue(quiz.improvingPossible());
    }
}
