package dorakdorak.infra.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 메모리에서 생성한 바이트 배열을 MultipartFile로 감싸는 클래스
 * ex) QR 코드와 같이 업로드 없이 생성된 파일 대응용
 */
public class MemoryMultipartFile implements MultipartFile {

  private final byte[] content;
  private final String originalFilename;
  private final String contentType;

  public MemoryMultipartFile(byte[] content, String originalFilename, String contentType) {
    this.content = content;
    this.originalFilename = originalFilename;
    this.contentType = contentType;
  }

  @Override
  public String getName() {
    return originalFilename;
  }

  @Override
  public String getOriginalFilename() {
    return originalFilename;
  }

  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public boolean isEmpty() {
    return content.length == 0;
  }

  @Override
  public long getSize() {
    return content.length;
  }

  @Override
  public byte[] getBytes() throws IOException {
    return content;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return new ByteArrayInputStream(content);
  }

  @Override
  public void transferTo(java.io.File dest) throws IOException {
    throw new UnsupportedOperationException("MemoryMultipartFile은 transferTo를 지원하지 않습니다.");
  }
}