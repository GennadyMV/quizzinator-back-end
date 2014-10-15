package app.services;

import app.domain.FileObject;
import app.repositories.ImageRepository;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageService {
    
    @Autowired
    private ImageRepository imageRepo;
    
    public byte[] getImageContent(Long id) {
        return imageRepo.findOne(id).getContent();
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
