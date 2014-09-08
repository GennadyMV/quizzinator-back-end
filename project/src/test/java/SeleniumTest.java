/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 *
 * @author albis
 */
public class SeleniumTest {

    private WebDriver driver;
    private String baseAddress;

    
    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", 
		"C:\\Koulu\\chromedriver_win32\\chromedriver.exe");
        this.driver = new ChromeDriver();
        this.baseAddress = "http://localhost:8080/";
    }

//    @Test
//    public void onceBobSubmittedElementAgeIsAvailable() {
//        // haetaan haluttu osoite (aiemmin määritelty muuttuja)
//        driver.get("http://www.google.com");
//        System.out.println("Page title is: " + driver.getTitle());
//    }
//    
//    @Test
//    public void koeTesti() {
//        driver.get(baseAddress);
//
//        Assert.assertTrue(driver.getPageSource().contains("Create a quiz"));
//
//    }

    @Test
    public void addTitle() {
        driver.get(baseAddress);

        WebElement element = driver.findElement(By.id("new-quiz-title"));
        element.sendKeys("Kysymys");
        element.submit();
        
        Assert.assertTrue(driver.getPageSource().contains("The quiz has been saved!"));

    }
}
