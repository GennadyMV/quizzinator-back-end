package app.services;

import app.domain.FileObject;
import app.repositories.ImageRepository;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageService {
    
    @Autowired
    private ImageRepository imageRepo;
    
    public ResponseEntity<byte[]> getImage(Long id) {
        FileObject image = imageRepo.findOne(id);
        
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.getMediaType()));
        headers.setContentLength(image.getSize());
        
        return new ResponseEntity<byte[]>(image.getContent(), headers, HttpStatus.CREATED);
    }
    
    public Long saveImage(MultipartFile file) throws IOException {
        FileObject image = new FileObject();
        image.setName(file.getOriginalFilename());
        image.setMediaType(file.getContentType());
        image.setSize(file.getSize());
        image.setContent(file.getBytes());
        imageRepo.save(image);
        
        return image.getId();
    }
}
