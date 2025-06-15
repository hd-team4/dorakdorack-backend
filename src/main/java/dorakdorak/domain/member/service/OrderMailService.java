package dorakdorak.domain.member.service;

import dorakdorak.domain.order.dto.response.OrderMailInfoDto;
import dorakdorak.domain.order.enums.OrderStatus;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderMailService {

  private final JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  private String senderEmail;

  public void sendOrderMail(List<OrderMailInfoDto> orderInfoList, OrderStatus status)
      throws MessagingException {
    if (orderInfoList.isEmpty()) {
      return;
    }

    String email = orderInfoList.get(0).getEmail();
    String memberName = orderInfoList.get(0).getMemberName();

    String title = getTitle(status);
    String body = getBody(status, memberName, orderInfoList);

    MimeMessage message = javaMailSender.createMimeMessage();
    message.setFrom(senderEmail);
    message.setRecipients(MimeMessage.RecipientType.TO, email);
    message.setSubject(title);
    message.setText(body, "UTF-8", "html");

    javaMailSender.send(message);
    log.info("주문 상태 메일 발송 완료: email={}, status={}", email, status);
  }

  private String getTitle(OrderStatus status) {
    return switch (status) {
      case PAYMENT_PENDING -> "[도락도락] 결제 대기 중입니다.";
      case PAYMENT_COMPLETED -> "[도락도락] 결제가 완료되었습니다.";
      case PAYMENT_FAILED -> "[도락도락] 결제에 실패하였습니다.";
      case PAYMENT_CANCELLED -> "[도락도락] 결제가 취소되었습니다.";
      case GONGGU_OPEN -> "[도락도락] 공동 구매의 모집이 진행중입니다.";
      case GONGGU_CANCELLED -> "[도락도락] 공동 구매가 취소되었습니다.";
      case DELIVERY_IN_PROGRESS -> "[도락도락] 배송이 시작되었습니다.";
      case DELIVERY_COMPLETED -> "[도락도락] 배송이 완료되었습니다.";
    };
  }

  private String getBody(OrderStatus status, String memberName, List<OrderMailInfoDto> items) {
    String statusText = getStatusText(status);
    String statusColor = getStatusColor(status);
    String actionMessage = getActionMessage(status);

    StringBuilder dosirakInfo = new StringBuilder();
    int totalQuantity = 0;
    for (OrderMailInfoDto item : items) {
      totalQuantity += item.getQuantity();
      dosirakInfo.append(String.format("""
          <tr>
              <td style="padding:12px 0;border-bottom:1px solid #f0f0f0;">
                  <div style="display:flex;align-items:center;">
                      <div style="flex:1;">
                          <h4 style="margin:0;font-size:16px;color:#333;">%s</h4>
                      </div>
                      <div style="text-align:right;color:#666;">
                          <span style="font-size:14px;">수량: %d개</span>
                      </div>
                  </div>
              </td>
          </tr>
          """, item.getDosirakName(), item.getQuantity()));
    }

    return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>도락도락 주문 알림</title>
            </head>
            <body style="margin:0;padding:0;background-color:#f8f9fa;font-family:'Pretendard',-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif;">
                <div style="max-width:600px;margin:0 auto;background-color:#ffffff;">
                    <!-- Header -->
                    <div style="padding:32px 24px;text-align:center;">
                        <img src="https://dorakdorak.store/images/logo2.png" alt="도락도락 로고" style="width:120px; margin-bottom:16px;" />
                        <h1 style="margin:0;color:#333;font-size:28px;font-weight:700;letter-spacing:-0.5px;">도락도락</h1>
                        <p style="margin:8px 0 0 0;color:#666;font-size:14px;">맛있는 도시락, 함께하는 즐거움</p>
                    </div>
                    
                    <!-- Status Badge -->
                    <div style="padding:24px 24px 0 24px;text-align:center;">
                        <div style="display:inline-block;background-color:%s;color:#ffffff;padding:12px 24px;border-radius:25px;font-weight:600;font-size:16px;margin-bottom:16px;">
                            %s
                        </div>
                    </div>
                    
                    <!-- Main Content -->
                    <div style="padding:0 24px 24px 24px;">
                        <div style="text-align:center;margin-bottom:32px;">
                            <h2 style="margin:0 0 8px 0;font-size:24px;color:#333;font-weight:600;">안녕하세요, %s님!</h2>
                            <p style="margin:0;font-size:16px;color:#666;line-height:1.5;">%s</p>
                        </div>
                        
                        <!-- Order Details Card -->
                        <div style="background-color:#f8f9fa;border-radius:12px;padding:24px;margin-bottom:24px;">
                            <h3 style="margin:0 0 16px 0;font-size:18px;color:#333;font-weight:600;display:flex;align-items:center;">
                                <span style="background-color:#667eea;width:4px;height:18px;border-radius:2px;margin-right:12px;"></span>
                                주문 상세 정보
                            </h3>
                            
                            <table style="width:100%%;border-collapse:collapse;">
                                %s
                            </table>
                            
                            <div style="margin-top:16px;padding-top:16px;border-top:2px solid #e9ecef;display:flex;justify-content:space-between;align-items:center;">
                                <span style="font-weight:600;color:#333;">총 수량</span>
                                <span style="font-size:18px;font-weight:700;color:#667eea;">%d개</span>
                            </div>
                        </div>
                        
                        <!-- Support Section -->
                        <div style="background-color:#fff;border:1px solid #e9ecef;border-radius:8px;padding:20px;text-align:center;">
                            <h4 style="margin:0 0 12px 0;color:#333;font-size:16px;">문의사항이 있으신가요?</h4>
                            <p style="margin:0 0 16px 0;color:#666;font-size:14px;">자세한 문의는 홈페이지를 참조해주세요!</p>
                            <a href="https://dorakdorak.store" style="background-color:#667eea;color:#ffffff;text-decoration:none;padding:12px 24px;border-radius:6px;font-weight:600;display:inline-block;">🏠 홈페이지 바로가기</a>
                        </div>
                    </div>
                    
                    <!-- Footer -->
                    <div style="background-color:#2c3e50;padding:24px;text-align:center;">
                        <p style="margin:0 0 8px 0;color:#ecf0f1;font-size:14px;font-weight:500;">도락도락과 함께해주셔서 감사합니다</p>
                        <p style="margin:0;color:#95a5a6;font-size:12px;">맛있는 도시락으로 행복한 하루 되세요!</p>
                        <div style="margin-top:16px;padding-top:16px;border-top:1px solid #34495e;">
                            <p style="margin:0;color:#95a5a6;font-size:11px;">© 2025 도락도락. All rights reserved.</p>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """,
        statusColor, statusText, memberName, actionMessage,
        dosirakInfo, totalQuantity);
  }

  private String getStatusText(OrderStatus status) {
    return switch (status) {
      case PAYMENT_PENDING -> "결제 대기중";
      case PAYMENT_COMPLETED -> "결제 완료";
      case PAYMENT_FAILED -> "결제 실패";
      case PAYMENT_CANCELLED -> "결제 취소";
      case GONGGU_OPEN -> "공구 모집중";
      case GONGGU_CANCELLED -> "공구 취소";
      case DELIVERY_IN_PROGRESS -> "배송중";
      case DELIVERY_COMPLETED -> "배송 완료";
    };
  }

  private String getStatusColor(OrderStatus status) {
    return switch (status) {
      case PAYMENT_PENDING -> "#ffc107";
      case PAYMENT_COMPLETED -> "#1e7e34";
      case PAYMENT_FAILED -> "#dc3545";
      case PAYMENT_CANCELLED -> "#6c757d";
      case GONGGU_OPEN -> "#17a2b8";
      case GONGGU_CANCELLED -> "#6c757d";
      case DELIVERY_IN_PROGRESS -> "#fd7e14";
      case DELIVERY_COMPLETED -> "#1e7e34";
    };
  }

  private String getActionMessage(OrderStatus status) {
    return switch (status) {
      case PAYMENT_PENDING -> "결제를 완료해주세요. 24시간 내 미결제시 주문이 자동 취소됩니다.";
      case PAYMENT_COMPLETED -> "결제가 성공적으로 완료되었습니다. 곧 맛있는 도시락을 받아보실 수 있어요!";
      case PAYMENT_FAILED -> "결제 처리 중 문제가 발생했습니다. 다시 시도해주세요.";
      case PAYMENT_CANCELLED -> "고객님의 요청으로 결제가 취소되었습니다.";
      case GONGGU_OPEN -> "공동구매 모집이 진행중입니다. 목표 인원 달성시 주문이 확정됩니다.";
      case GONGGU_CANCELLED -> "공동구매 모집이 취소되었습니다. 결제금액은 자동으로 환불됩니다.";
      case DELIVERY_IN_PROGRESS -> "도시락이 배송 중입니다. 곧 맛있게 드실 수 있어요!";
      case DELIVERY_COMPLETED -> "도시락이 성공적으로 배송되었습니다. 맛있게 드세요!";
    };
  }
}
