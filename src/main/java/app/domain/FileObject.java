package app.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * File saved to the database.
 * Used for pictures uploaded by quiz creators.
 */
@Entity
public class FileObject extends AbstractPersistable<Long> {
    /**
     * Original filename of the uploaded file.
     */
    private String name;
    
    /**
     * mime-type of the file
     */
    private String mediaType;
    
    /**
     * file size in bytes
     */
    private Long size; 

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getMediaType() {
        return mediaType;
    }
    
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
    
    public Long getSize() {
        return size;
    }
    
    public void setSize(Long size) {
        this.size = size;
    }
    
    public byte[] getContent() {
        return content;
    }
    
    public void setContent(byte[] content) {
        this.content = content;
    }
}
