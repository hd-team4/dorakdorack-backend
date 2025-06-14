package dorakdorak.domain.dosirak.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyCustomDosirakResponseDto {

  String name;
  String imageUrl;
  LocalDateTime createdAt;
}
