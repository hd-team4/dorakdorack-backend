package dorakdorak.domain.zeroWaste.service;

import dorakdorak.domain.zeroWaste.dto.response.UniversityRankingResponse;
import dorakdorak.domain.zeroWaste.dto.response.ZeroWasteJoinResponse;
import dorakdorak.domain.zeroWaste.dto.response.ZeroWasteResultResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ZeroWasteService {

  ZeroWasteJoinResponse getJoinInfo(String qrcode);

  ZeroWasteResultResponse verifyEmptyDosirak(String qrcode, MultipartFile imageFile);

  UniversityRankingResponse getUniversityRankings();
}
