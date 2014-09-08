import com.gargoylesoftware.htmlunit.BrowserVersion;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;


public class SeleniumTest {
    private WebDriver driver;
    private String baseAddress;
    private String port;

    @Before
    public void setUp() {
        HtmlUnitDriver hud = new HtmlUnitDriver(BrowserVersion.FIREFOX_24);
        hud.setJavascriptEnabled(true);
        
        this.driver = hud;
        
        port = "8080";
        this.baseAddress = "http://localhost:" + port + "/";
        
        System.out.println(baseAddress);
    }

    @Test
    public void onceBobSubmittedElementAgeIsAvailable() {
        // haetaan haluttu osoite (aiemmin määritelty muuttuja)
        driver.get(baseAddress);
        System.out.println("Page title is: " + driver.getTitle());
    }
    
//    @Test
//    public void koeTesti() {
//        driver.get(baseAddress);
//
//        Assert.assertTrue(driver.getPageSource().contains("Create a quiz"));
//
//    }
//
    @Test
    public void addTitle() {
        driver.get(baseAddress);
        
        this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
        WebElement element = driver.findElement(By.id("testtest"));
        
        assertNotNull(element);
        
        /*element.sendKeys("Kysymys");
        element.submit();*/
        
        //Assert.assertTrue(driver.getPageSource().contains("The quiz has been saved!"));

    }
}
