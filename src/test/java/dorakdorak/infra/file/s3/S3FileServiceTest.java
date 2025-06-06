package dorakdorak.infra.file.s3;

import dorakdorak.infra.file.FileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class S3FileServiceTest {

    @Autowired
    private FileService fileService;

    @Autowired
    private com.amazonaws.services.s3.AmazonS3 amazonS3;

    @org.springframework.beans.factory.annotation.Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Test
    @DisplayName("S3 업로드 성공")
    void upload_success() throws Exception {
        // 테스트 리소스 경로에서 이미지 파일 로딩
        InputStream imageStream = getClass().getResourceAsStream("/static/test-image.jpg");
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.jpg",
                "image/jpeg",
                imageStream
        );

        // S3에 업로드 수행
        String url = fileService.upload(file, "test-folder", "test");

        // 업로드된 URL 확인
        System.out.println("업로드 URL: " + url);
        assertThat(url).isNotBlank();
        assertThat(url).contains("https://");
    }

    @Test
    @DisplayName("S3 삭제 성공")
    void delete_success() throws Exception {

        // 먼저 파일 업로드
        InputStream imageStream = getClass().getResourceAsStream("/static/test-image.jpg");
        MockMultipartFile file = new MockMultipartFile(
                "file", "test-image.jpg", "image/jpeg", imageStream
        );
        String uploadedUrl = fileService.upload(file, "test-folder", "delete-test");

        // 삭제 요청
        fileService.delete(uploadedUrl);
        System.out.println("삭제 요청 완료: " + uploadedUrl);

        // 실제로 삭제되었는지 확인
        String key = uploadedUrl.substring(uploadedUrl.indexOf(".com/") + 5);
        boolean exists = amazonS3.doesObjectExist(bucket, key);
        assertThat(exists).isFalse();
    }
}
