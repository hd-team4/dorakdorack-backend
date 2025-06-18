package dorakdorak.domain.zeroWaste.service;

import dorakdorak.domain.member.mapper.MemberMapper;
import dorakdorak.domain.order.dto.OrderDto;
import dorakdorak.domain.order.dto.OrderItemDto;
import dorakdorak.domain.order.mapper.OrderMapper;
import dorakdorak.domain.zeroWaste.dto.response.UniversityRankingResponse;
import dorakdorak.domain.zeroWaste.dto.UniversityRankingDto;
import dorakdorak.domain.zeroWaste.dto.response.ZeroWasteJoinResponse;
import dorakdorak.domain.zeroWaste.dto.response.ZeroWasteResultResponse;
import dorakdorak.domain.zeroWaste.mapper.ZeroWasteMapper;
import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;
import dorakdorak.global.error.exception.EntityNotFoundException;
import dorakdorak.global.error.exception.InvalidValueException;
import dorakdorak.global.util.jwt.QrTokenProvider;
import dorakdorak.infra.file.s3.S3FileService;
import io.jsonwebtoken.Claims;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ZeroWasteServiceImpl implements ZeroWasteService {

  private final QrTokenProvider qrTokenProvider;
  private final S3FileService s3FileService;
  private final DosirakRemainAnalyzer dosirakRemainAnalyzer;
  private final OrderMapper orderMapper;
  private final MemberMapper memberMapper;
  private final ZeroWasteMapper zeroWasteMapper;

  @Override
  public ZeroWasteJoinResponse getJoinInfo(String qrcode) {
    Claims claims = qrTokenProvider.validateQrToken(qrcode);
    Long orderId = ((Number) claims.get("orderId")).longValue();
    Long orderItemId = ((Number) claims.get("orderItemId")).longValue();

    validateQrToken(qrcode); // 이미 사용된 QR 코드인지 검증

    // 주문 정보 조회
    OrderDto order = orderMapper.findById(orderId)
        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ORDER_NOT_FOUND.getMessage(),
            ErrorCode.ORDER_NOT_FOUND));
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
    String date = order.getArrivalAt().format(formatter);

    // 주문 아이템 정보 조회
    OrderItemDto item = orderMapper.findItemById(orderItemId)
        .orElseThrow(() -> new EntityNotFoundException(orderItemId + "이 존재하지 않습니다.",
            ErrorCode.ORDER_ITEM_NOT_FOUND));

    return new ZeroWasteJoinResponse(item.getName(), date, item.getImageUrl());
  }

  @Override
  public ZeroWasteResultResponse verifyEmptyDosirak(String qrcode, MultipartFile image) {
    // 1. 토큰 검증 + claim 추출
    Claims claims = qrTokenProvider.validateQrToken(qrcode);
    Long orderItemId = ((Number) claims.get("orderItemId")).longValue();
    Long memberId = ((Number) claims.get("memberId")).longValue();

    // 2. 재사용 여부 확인
    validateQrToken(qrcode);

    // 3. 이미지 업로드
    String imageUrl = s3FileService.upload(image, "zero-waste", "proof_" + orderItemId);

    // 4. 잔반 분석
    int remainPercentage = dosirakRemainAnalyzer.analyze(imageUrl);

    // 5. 인증 여부 판단
    boolean isAccepted = remainPercentage <= 10;

    if (isAccepted) {
      memberMapper.incrementZeroWasteCount(memberId);
      orderMapper.clearQrToken(orderItemId);
    }

    String result = isAccepted ? "ACCEPTED" : "REJECTED";
    String message = isAccepted
        ? "잔반이 거의 없어 인증에 성공했습니다!"
        : "잔반이 많아 인증에 실패했습니다. 다시 인증해주세요!";

    return new ZeroWasteResultResponse(result, remainPercentage, message);
  }

  @Override
  @Transactional(readOnly = true)
  public UniversityRankingResponse getUniversityRankings() {
    List<UniversityRankingDto> rankings = zeroWasteMapper.findUniversityRankings();
    if (rankings == null || rankings.isEmpty()) {
      throw new BusinessException(ErrorCode.UNIVERSITY_RANKING_ERROR);
    }
    return new UniversityRankingResponse(rankings);
  }

  private void validateQrToken(String qrcode) {
    orderMapper.findItemByQrToken(qrcode)
        .orElseThrow(
            () -> new InvalidValueException(ErrorCode.QR_CODE_ALREADY_USED.getMessage(), ErrorCode.QR_CODE_ALREADY_USED));
  }
}
