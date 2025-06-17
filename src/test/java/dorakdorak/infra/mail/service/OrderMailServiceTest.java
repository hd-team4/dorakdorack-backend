package dorakdorak.infra.mail.service;

import dorakdorak.domain.order.dto.OrderMailInfoDto;
import dorakdorak.domain.order.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class OrderMailServiceTest {

  @Autowired
  private OrderMailSender orderMailSender;

  @Test
  void 주문상태_메일이_정상적으로_도착해야한다() throws InterruptedException {
    String testEmail = "hwjun12@naver.com";
    String testName = "홍길동";

    List<OrderMailInfoDto> items = List.of(
        new OrderMailInfoDto(testEmail, testName, "닭가슴살 도시락", 2L),
        new OrderMailInfoDto(testEmail, testName, "소불고기 도시락", 1L)
    );

    // 주문 상태 완료 → 발송 메시지 구성
    orderMailSender.sendOrderMail(items, OrderStatus.PAYMENT_COMPLETED);

    System.out.println("메일 발송 요청 완료 → RabbitMQ로 메시지 전송됨.");
    System.out.println("소비자에서 처리되기를 기다립니다. 메일함을 확인해보세요...");
  }
}
