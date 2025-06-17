package dorakdorak.infra.file.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;
import dorakdorak.global.error.exception.InvalidValueException;
import dorakdorak.infra.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3FileService implements FileService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 허용 확장자 : .jpg, .jpeg, .png, .webp
    private static final List<String> ALLOWED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".webp");

    // 허용된 MIME 타입 : image/jpeg, image/png, image/webp
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpeg", "image/png", "image/webp");

    // 허용 파일 크기 : 최대 5MB
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    @Override
    public String upload(MultipartFile file, String folder, String baseFileName) {
        validateFile(file);

        String extension = getExtension(file.getOriginalFilename());
        String key = folder + "/" + baseFileName + "_" + UUID.randomUUID() + extension;

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(new PutObjectRequest(bucket, key, file.getInputStream(), metadata));

            return amazonS3.getUrl(bucket, key).toString();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Override
    public void delete(String imageUrl) {
        String key = getImageKey(imageUrl);
        amazonS3.deleteObject(bucket, key);
    }

    // 유효성 검증
    private void validateFile(MultipartFile file) {
        String extension = getExtension(file.getOriginalFilename()).toLowerCase();
        String contentType = file.getContentType();

        if (!ALLOWED_EXTENSIONS.contains(extension) || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new BusinessException(ErrorCode.INVALID_FILE_EXTENSION);
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(ErrorCode.FILE_LIMIT_EXCEEDED);
        }
    }

    // 확장자 추출
    private String getExtension(String originalFilename) {
        int idx = originalFilename.lastIndexOf(".");
        return idx != -1 ? originalFilename.substring(idx) : "";
    }

    // url에서 이미지 키 추출
    private String getImageKey(String imageUrl) {
        /*
        예를 들어
        https://your-bucket.s3.amazonaws.com/images/profile/profile_abc123.jpg
        이면 "images/profile/profile_abc123.jpg"를 추출하여 리턴
         */
        return imageUrl.substring(imageUrl.indexOf(".com/") + 5);
    }
}
