package dorakdorak.infra.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;
import java.io.ByteArrayOutputStream;
import java.util.Map;

public class QrCodeGenerator {

  public static ByteArrayOutputStream generate(String text, int width, int height) {
    try {
      Map<EncodeHintType, Object> hints = Map.of(EncodeHintType.CHARACTER_SET, "UTF-8");
      BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      MatrixToImageWriter.writeToStream(matrix, "PNG", os);
      return os;
    } catch (Exception e) {
      throw new BusinessException(ErrorCode.QR_CODE_GENERATION_FAILED);
    }
  }
}