package dorakdorak.infra.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String upload(MultipartFile file, String folder, String baseFileName);
    void delete(String key);
}
