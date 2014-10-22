package app.controllers;

import app.Application;
import app.repositories.ImageRepository;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class ImageControllerTest {
    
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    
    @Autowired
    private ImageRepository imageRepo;
    
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    @Test
    @DirtiesContext
    public void fileWithIncorrectTypeNotAdded() throws Exception {
        assertEquals(0, imageRepo.count());
        
        MockMultipartFile multipartFile = new MockMultipartFile("image", "file.txt",
                                        "text/plain", "123".getBytes());
        
        this.mockMvc.perform(fileUpload("/images").file(multipartFile));
        
        assertEquals(0, imageRepo.count());
    }
    
    @Test
    @DirtiesContext
    public void fileWIthCorrectTypeAdded() throws Exception {
        assertEquals(0, imageRepo.count());
        
        MockMultipartFile multipartFile = new MockMultipartFile("image", "file.png",
                                        "image/png", "123".getBytes());
        
        this.mockMvc.perform(fileUpload("/images").file(multipartFile));
        
        assertEquals(1, imageRepo.count());
        assertTrue(imageRepo.findOne(1L).getMediaType().equals("image/png"));
    }
    
    @Test
    @DirtiesContext
    public void multipleImagesAdded() throws Exception {
        assertEquals(0, imageRepo.count());
        MockMultipartFile multipartFile = new MockMultipartFile("image", "file.gif",
                                        "image/gif", "123".getBytes());
        this.mockMvc.perform(fileUpload("/images").file(multipartFile));
        this.mockMvc.perform(fileUpload("/images").file(multipartFile));
        this.mockMvc.perform(fileUpload("/images").file(multipartFile));
        this.mockMvc.perform(fileUpload("/images").file(multipartFile));
        
        assertEquals(4, imageRepo.count());
    }
    
    @Test
    @DirtiesContext
    public void postImageReturnsCorrectUrl() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("image", "kissa.jpeg",
                                        "image/jpeg", "salama".getBytes());
        this.mockMvc.perform(fileUpload("/images").file(multipartFile));
        this.mockMvc.perform(fileUpload("/images").file(multipartFile));
        MvcResult result = this.mockMvc.perform(fileUpload("/images").file(multipartFile))
                .andReturn();
        
        assertTrue(result.getResponse().getForwardedUrl().equals("images/3"));
    }
}