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
import org.openqa.selenium.WebDriver;
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
        this.driver = new HtmlUnitDriver();
        this.baseAddress = "...";
    }

    @Test
    public void onceBobSubmittedElementAgeIsAvailable() {
        // haetaan haluttu osoite (aiemmin määritelty muuttuja)
        driver.get("http://www.google.com");
        System.out.println("Page title is: " + driver.getTitle());
    }
}
