package app.repositories;

import app.domain.FileObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<FileObject, Long>{
}
