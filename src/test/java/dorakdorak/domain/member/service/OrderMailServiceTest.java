package dorakdorak.domain.member.service;

import dorakdorak.domain.order.dto.OrderMailInfoDto;
import dorakdorak.domain.order.enums.OrderStatus;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(OrderMailService.class)
class OrderMailServiceTest {

  @Autowired
  private OrderMailService orderMailService;

  @Test
  void sendTestEmail() {
    List<OrderMailInfoDto> items = List.of(
        new OrderMailInfoDto("test@test.com", "홍길동", "건강 도시락", 4L),
        new OrderMailInfoDto("test@test.com", "홍길동", "도락 도시락", 4L),
        new OrderMailInfoDto("test@test.com", "홍길동", "불고기 도시락", 2L),
        new OrderMailInfoDto("test@test.com", "홍길동", "다이어트 도시락", 1L)
    );

    orderMailService.sendOrderMail(items, OrderStatus.PAYMENT_COMPLETED);

  }
}

