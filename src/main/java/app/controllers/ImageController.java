package app.controllers;

import app.services.ImageService;
import java.awt.Image;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    
    //TODO: fix this to support every file!!
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "image/jpeg")
    public ResponseEntity<byte[]> showImage(@PathVariable Long id) {
//        MultiValueMap<String, String> headers = new HashMap();
//        final HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_JPEG);
//        headers.get
        
        ResponseEntity<byte[]> re = new ResponseEntity<byte[]>(imageService.getImageContent(id), HttpStatus.OK);
        
        return re;
    
//        return imageService.getImageContent(id);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String addImage(@RequestParam("image") MultipartFile file) throws IOException {
        if (!file.getContentType().equals("image/gif") &&
            !file.getContentType().equals("image/jpeg") &&
            !file.getContentType().equals("image/png")) {
            
            return "/quiz";
        }
        
        return "redirect:/images/" + imageService.saveImage(file);
    }
}
