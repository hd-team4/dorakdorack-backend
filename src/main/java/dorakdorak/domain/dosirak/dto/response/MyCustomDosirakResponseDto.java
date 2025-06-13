package dorakdorak.domain.dosirak.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyCustomDosirakResponseDto {
    String name;
    String imageUrl;
    LocalDateTime createdAt;
}
