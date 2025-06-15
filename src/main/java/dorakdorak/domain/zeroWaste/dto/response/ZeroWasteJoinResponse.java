package dorakdorak.domain.zeroWaste.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZeroWasteJoinResponse {
  private String dosirakName;
  private String expireDate;
  private String imageUrl;
}