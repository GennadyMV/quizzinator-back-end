package app.controllers;

import app.Application;
import app.repositories.ImageRepository;
import java.util.Arrays;
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
        
        this.mockMvc.perform(fileUpload("/images").file(multipartFile)).andReturn();
        
        assertEquals(0, imageRepo.count());
    }
    
    @Test
    @DirtiesContext
    public void pngAdded() throws Exception {
        assertEquals(0, imageRepo.count());
        
        MockMultipartFile multipartFile = new MockMultipartFile("image", "file.png",
                                        "image/png", "123".getBytes());
        
        this.mockMvc.perform(fileUpload("/images").file(multipartFile)).andReturn();
        
        MvcResult result = this.mockMvc.perform(get("/images/1")).andReturn();
        assertEquals(1, imageRepo.count());
        assertTrue(Arrays.equals(result.getResponse().getContentAsByteArray(), "123".getBytes()));
        assertTrue(result.getResponse().getContentType().equals("image/png"));
        assertTrue(result.getResponse().getHeader("filename").equals("file.png"));
        assertEquals("123".getBytes().length, result.getResponse().getContentLength());
    }
    
    @Test
    @DirtiesContext
    public void jpegAdded() throws Exception {
        assertEquals(0, imageRepo.count());
        
        MockMultipartFile multipartFile = new MockMultipartFile("image", "file.jpeg",
                                        "image/jpeg", "koiruli".getBytes());
        
        this.mockMvc.perform(fileUpload("/images").file(multipartFile)).andReturn();
        
        MvcResult result = this.mockMvc.perform(get("/images/1")).andReturn();
        assertEquals(1, imageRepo.count());
        assertTrue(Arrays.equals(result.getResponse().getContentAsByteArray(), "koiruli".getBytes()));
        assertTrue(result.getResponse().getContentType().equals("image/jpeg"));
        assertTrue(result.getResponse().getHeader("filename").equals("file.jpeg"));
        assertEquals("koiruli".getBytes().length, result.getResponse().getContentLength());
    }
    
    @Test
    @DirtiesContext
    public void gifAdded() throws Exception {
        assertEquals(0, imageRepo.count());
        
        MockMultipartFile multipartFile = new MockMultipartFile("image", "file.gif",
                                        "image/gif", "muah".getBytes());
        
        this.mockMvc.perform(fileUpload("/images").file(multipartFile)).andReturn();
        
        MvcResult result = this.mockMvc.perform(get("/images/1")).andReturn();
        assertEquals(1, imageRepo.count());
        assertTrue(Arrays.equals(result.getResponse().getContentAsByteArray(), "muah".getBytes()));
        assertTrue(result.getResponse().getContentType().equals("image/gif"));
        assertTrue(result.getResponse().getHeader("filename").equals("file.gif"));
        assertEquals("muah".getBytes().length, result.getResponse().getContentLength());
    }
}