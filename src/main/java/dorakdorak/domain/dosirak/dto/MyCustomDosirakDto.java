package dorakdorak.domain.dosirak.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyCustomDosirakDto {

  String name;
  String imageUrl;
  LocalDateTime createdAt;
}
