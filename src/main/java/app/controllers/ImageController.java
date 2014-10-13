package app.controllers;

import app.domain.FileObject;
import app.repositories.ImageRepository;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("images")
public class ImageController {
    
    @Autowired
    private ImageRepository imageRepo;
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public byte[] showImage(@PathVariable Long id) {
        return imageRepo.findOne(id).getContent();
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String addImage(@RequestParam("image") MultipartFile file) throws IOException {
        if (!file.getContentType().equals(MediaType.IMAGE_GIF_VALUE) &&
            !file.getContentType().equals(MediaType.IMAGE_JPEG_VALUE) &&
            !file.getContentType().equals(MediaType.IMAGE_PNG_VALUE)) {
            
            return ""+HttpStatus.UNSUPPORTED_MEDIA_TYPE.value();
        }
        
        FileObject image = new FileObject();
        image.setName(file.getOriginalFilename());
        image.setMediaType(file.getContentType());
        image.setSize(file.getSize());
        image.setContent(file.getBytes());
        imageRepo.save(image);
        
        return "images/" + image.getId();
    }
}
