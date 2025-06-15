package dorakdorak.infra.payment.toss;

import lombok.Getter;

@Getter
public class TossErrorResponse {

    private String code; // 에러 코드
    private String message; // 에러 메세지
}