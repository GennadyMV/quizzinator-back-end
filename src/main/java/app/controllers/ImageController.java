package app.controllers;

import app.services.ImageService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/images")
public class ImageController {
    
    @Autowired
    private ImageService imageService;
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "image/gif")
    public ResponseEntity<byte[]> showImage(@PathVariable Long id) {
        return imageService.getImage(id);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String addImage(@RequestParam("image") MultipartFile file) throws IOException {
        if (!file.getContentType().equals("image/gif") &&
            !file.getContentType().equals("image/jpeg") &&
            !file.getContentType().equals("image/png")) {
            
            return "";
        }
        
        return "images/" + imageService.saveImage(file);
    }
}
