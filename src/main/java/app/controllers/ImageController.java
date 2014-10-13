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
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        FileObject fo = imageRepo.findOne(id);
        
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fo.getMediaType()));
        headers.setContentLength(fo.getSize());
        
        return new ResponseEntity<byte[]>(fo.getContent(), headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String addImage(@RequestParam("image") MultipartFile file) throws IOException {
        if (!file.getContentType().equals(MediaType.IMAGE_GIF_VALUE) &&
            !file.getContentType().equals(MediaType.IMAGE_JPEG_VALUE) &&
            !file.getContentType().equals(MediaType.IMAGE_PNG_VALUE)) {
            
            return ""+HttpStatus.UNSUPPORTED_MEDIA_TYPE.value();
        }
        
        FileObject fo = new FileObject();
        fo.setName(file.getOriginalFilename());
        fo.setMediaType(file.getContentType());
        fo.setSize(file.getSize());
        fo.setContent(file.getBytes());
        imageRepo.save(fo);
        
        return "images/" + fo.getId();
    }
}
