package dorakdorak.infra.mail.consumer;

import dorakdorak.domain.order.dto.OrderMailInfoDto;
import dorakdorak.domain.order.enums.OrderStatus;
import dorakdorak.infra.mail.dto.OrderMailMessage;
import dorakdorak.global.util.mail.OrderMailContentBuilder;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderMailConsumer {

  private final JavaMailSender javaMailSender;
  private final OrderMailContentBuilder contentBuilder;

  @Value("${spring.mail.username}")
  private String senderEmail;

  @RabbitListener(queues = "${queue.order}")
  public void handle(OrderMailMessage message) {
    try {
      List<OrderMailInfoDto> items = message.items();
      OrderStatus status = message.status();
      String email = message.email();
      String memberName = message.memberName();

      String title = contentBuilder.getTitle(status);
      String body = contentBuilder.getBody(status, memberName, items);

      MimeMessage mail = javaMailSender.createMimeMessage();
      mail.setFrom(senderEmail);
      mail.setRecipients(MimeMessage.RecipientType.TO, email);
      mail.setSubject(title);
      mail.setText(body, "UTF-8", "html");
      javaMailSender.send(mail);

      log.info("RabbitMQ로 메일 발송 완료: {}", email);
    } catch (Exception e) {
      log.error("RabbitMQ 메일 발송 실패: {}", message.email(), e);
    }
  }
}
