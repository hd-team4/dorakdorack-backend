package dorakdorak.infra.mail.service;

import dorakdorak.domain.order.dto.OrderMailInfoDto;
import dorakdorak.domain.order.enums.OrderStatus;
import java.util.List;

public interface OrderMailSender extends MailSender {
  void sendOrderMail(List<OrderMailInfoDto> orderInfoList, OrderStatus status);
}

