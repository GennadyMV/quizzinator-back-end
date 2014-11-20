package app.services;

import app.repositories.QuizAnswerRepository;
import app.repositories.QuizRepository;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QuizServiceTest {
    @InjectMocks
    private QuizService qs;
    
    @Mock
    private QuizRepository qr;
    
    @Mock
    private QuizAnswerRepository qar;
    
    public QuizServiceTest() {
        qs = new QuizService();
        qr = Mockito.mock(QuizRepository.class);
        qar = Mockito.mock(QuizAnswerRepository.class);
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testValidateAnswerQuizCombination() {
    }

    @Test
    public void testIsValidAnswerQuizCombinationWithNonExistentQuizAndAnswer() {
        Mockito.when(qr.findOne(1L)).thenReturn(null);
        Mockito.when(qar.findOne(1L)).thenReturn(null);
        assertFalse(qs.isValidAnswerQuizCombination(1L, 1L));
    }

    @Test
    public void testSubmitAnswer() {
    }

    @Test
    public void testGetAnswersForReview_Quiz_User() {
    }

    @Test
    public void testGetAnswersForReview_3args() {
    }

    @Test
    public void testGetReviewsByAnswer() {
    }

    @Test
    public void testGetQuizForUsername() {
    }

    @Test
    public void testAddPlaceHolderAnswer() {
    }

    @Test
    public void testGetPlaceholderAnswers() {
    }

    @Test
    public void testDeleteAnswer() {
    }
    
}
