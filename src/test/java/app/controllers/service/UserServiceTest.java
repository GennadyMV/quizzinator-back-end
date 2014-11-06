package app.controllers.service;

import app.Application;
import app.domain.User;
import app.services.UserService;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class UserServiceTest {
    
    @Autowired
    private WebApplicationContext wac;
    
    @Autowired
    private UserService userService;
    
    public UserServiceTest() {
    }
    
    @Before
    public void setUp() {
        userService.getOrCreateUser("masa");
        userService.getOrCreateUser("eero");
    }

    @Test
    @DirtiesContext
    public void cannotGetUserWithNull() {
        User user = null;
        user = userService.getUser(user);
        
        assertEquals(null, user);
        
        String username = null;
        user = userService.getUser(username);
        
        assertEquals(null, user);
    }
    
    @Test
    @DirtiesContext
    public void nullReturnedWhenGetWithNonExistentUserName() {
        String username = "keke";
        User user = userService.getUser(username);
        assertEquals(null, user);
    }
    
    @Test
    @DirtiesContext
    public void userFoundWithUserAndUsername() {
        String username = "masa";
        User user = new User();
        user.setName("masa");
        
        User withUsername = userService.getUser(username);
        User withUser = userService.getUser(user);
        
        assertTrue(withUsername.getName().equals("masa"));
        assertEquals(withUsername.getName(), withUser.getName());
    }
}
