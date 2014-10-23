package app.controllers;

import app.services.ImageService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/images")
public class ImageController {
    
    @Autowired
    private ImageService imageService;
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> showImage(@PathVariable Long id) {
        return imageService.getImage(id);
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public Map<String, Long> addImage(@RequestParam("image") MultipartFile file) throws IOException {
        Long imageId = imageService.saveImage(file);
        
        Map<String, Long> imageData = new HashMap<String, Long>();
        imageData.put("imageId", imageId);
        
        return imageData;
    }
}
