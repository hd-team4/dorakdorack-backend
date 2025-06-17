package dorakdorak.infra.mail.service;

import dorakdorak.domain.order.dto.OrderMailInfoDto;
import dorakdorak.domain.order.enums.OrderStatus;
import dorakdorak.infra.mail.dto.OrderMailMessage;
import dorakdorak.infra.mail.producer.OrderMailProducer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderMailService implements OrderMailSender {

  private final OrderMailProducer orderMailProducer;

  @Override
  public void sendOrderMail(List<OrderMailInfoDto> orderInfoList, OrderStatus status) {
    if (orderInfoList.isEmpty()) return;

    try {
      OrderMailMessage message = new OrderMailMessage(orderInfoList, status);
      orderMailProducer.send(message);
      log.info("RabbitMQ에 주문 상태 알림을 전송했습니다: 수신자={}, 메시지 내용={}", message.memberName(), message.email());
    } catch (Exception e) {
      log.error("RabbitMQ 메시지 전송 실패: 주문상태={}, 에러={}", status, e.getMessage(), e);
    }
  }
}
