package dorakdorak.infra.mail.producer;

import dorakdorak.infra.mail.dto.OrderMailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMailProducer {

  private final RabbitTemplate rabbitTemplate;

  @Value("${queue.order}")
  private String queueName;

  public void send(OrderMailMessage message) {
    rabbitTemplate.convertAndSend(queueName, message);
  }
}