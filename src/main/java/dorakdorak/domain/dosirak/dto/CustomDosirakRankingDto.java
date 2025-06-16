package dorakdorak.domain.dosirak.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomDosirakRankingDto {

  private Long dosirakId;
  private String name;
  private Integer price;
  private Integer vote;
  private String imageUrl;
  private Boolean isVoted;
}
