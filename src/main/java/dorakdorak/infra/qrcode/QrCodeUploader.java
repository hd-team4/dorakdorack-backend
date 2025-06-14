package dorakdorak.infra.qrcode;

import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.InvalidValueException;
import dorakdorak.infra.file.FileService;
import dorakdorak.infra.file.MemoryMultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

@Component
@RequiredArgsConstructor
public class QrCodeUploader {

  private final FileService fileService;

  private static final String FOLDER = "qr";
  private static final String BASE_FILE_NAME = "zero-waste";

  public String uploadQrCodeToS3(String text) {
    try {
      // 1. QR 코드 이미지 생성
      ByteArrayOutputStream qrImage = QrCodeGenerator.generate(text, 300, 300);
      byte[] content = qrImage.toByteArray();

      // 2. MultipartFile로 래핑
      MultipartFile multipartFile = new MemoryMultipartFile(content, BASE_FILE_NAME + ".png", "image/png");

      // 3. 기존 S3FileService 사용
      return fileService.upload(multipartFile, FOLDER, BASE_FILE_NAME);

    } catch (Exception e) {
      throw new InvalidValueException("S3 파일 업로드 중 오류 발생", ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }
}