package dorakdorak.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticPopularResponseDto {

  private int rank;
  private Long dosirakId;
  private String name;
  private String imageUrl;
  private int count;
}
