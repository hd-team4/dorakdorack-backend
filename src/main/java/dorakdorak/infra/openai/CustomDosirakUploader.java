package dorakdorak.infra.openai;

import dorakdorak.infra.file.FileService;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import lombok.RequiredArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomDosirakUploader {

  private final FileService fileService;
  private static final String FOLDER = "custom";
  private static final String BASE_FILE_NAME = "dosirak";

  public String uploadCustomDosirak(String imageUrl) {
    try {
      // 1. 이미지 URL 연결
      URL url = new URL(imageUrl);
      URLConnection connection = url.openConnection();

      String contentType = connection.getContentType(); // image/jpeg 등

      // 2. 파일 스트림 가져오기
      try (InputStream inputStream = connection.getInputStream()) {

        // 3. MultipartFile로 변환
        MockMultipartFile multipartFile = new MockMultipartFile(
            "custom-dosirak.jpg",
            "custom-dosirak.jpg",
            contentType,
            inputStream
        );
        return fileService.upload(multipartFile, FOLDER, BASE_FILE_NAME);
      }

    } catch (Exception e) {
      throw new RuntimeException("이미지 업로드 중 오류 발생: " + imageUrl, e);
    }
  }
}
