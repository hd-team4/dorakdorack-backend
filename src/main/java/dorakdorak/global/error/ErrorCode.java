package dorakdorak.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

  /* QR CODE ERROR */
  QR_CODE_GENERATION_FAILED(500, "QR001", "QR 코드 생성에 실패했습니다."),

  /* TOSS PAYMENTS ERROR */
  TOSS_CLIENT_ERROR(400, "TOSS001", "잘못된 결제 요청입니다."),
  TOSS_SERVER_ERROR(500, "TOSS002", "토스 서버 오류입니다."),

  /* FILE ERROR */
  INVALID_FILE_EXTENSION(400, "FILE001", "지원하지 않는 파일 확장자입니다."),
  FILE_UPLOAD_FAILED(500, "FILE002", "파일 업로드에 실패했습니다."),

  /* OPEN AI ERROR */
  OPENAI_RESPONSE_ERROR(500, "OPENAI001", "OpenAI 응답 처리 중 오류가 발생했습니다."),
  OPENAI_API_CALL_FAILED(500, "OPENAI002", "OpenAI API 호출에 실패했습니다."),
  OPENAI_RESULT_PARSING_FAILED(500, "OPENAI003", "OpenAI 결과 파싱에 실패했습니다."),

  /* COMMON ERROR */
  INTERNAL_SERVER_ERROR(500, "COMMON001", "Internal Server Error"),
  INVALID_INPUT_VALUE(400, "COMMON002", "Invalid Input Value"),
  ENTITY_NOT_FOUND(400, "COMMON003", "Entity Not Found"),

  /* MEMBER ERROR */
  DUPLICATED_ID(400, "MEM001", "It Is Duplicated ID"),
  DUPLICATED_EMAIL(400, "MEM002", "It Is Duplicated Email"),
  MEMBER_NOT_FOUND(400, "MEM003", "Member Not Found"),
  PASSWORD_NOT_MATCH(400, "MEM004", "Password Not Match"),

  /* AUTH ERROR */
  INVALID_PASSWORD(400, "AUTH001", "It Is Invalid Password"),
  INVALID_TOKEN(400, "AUTH002", "It Is Invalid Token"),
  INVALID_BEARER_PREFIX(400, "AUTH003", "It Is Invalid Bearer Prefix"),
  UNAUTHORIZED(401, "AUTH004", "Unauthorized, Please Login"),
  FORBIDDEN(403, "AUTH005", "Forbidden, You Don't Have Any Authority"),
  REFRESH_TOKEN_MISSING(401, "AUTH006", "리프레시 토큰이 존재하지 않습니다."),
  REFRESH_TOKEN_EXPIRED(401, "AUTH007", "리프레시 토큰이 만료되었습니다."),
  REFRESH_TOKEN_INVALID_CATEGORY(401, "AUTH008", "유효하지 않은 토큰 타입입니다."),
  REFRESH_TOKEN_NOT_FOUND(401, "AUTH009", "서버에 등록되지 않은 리프레시 토큰입니다."),

  /* EMAIL ERROR */
  UNABLE_TO_SEND_EMAIL(500, "EMAIL001", "Unable To Send Email"),
  TOO_MANY_EMAIL_VERIFICATION_REQUESTS(429, "EMAIL002", " exceeded the number of allowed email verification requests. Please try again after 24 hours."),

  /* DOSIRAK ERROR */
  DOSIRAK_DATA_ACCESS_ERROR(500, "DOSIRAK001", "도시락 데이터 조회 중 오류가 발생했습니다."),
  DOSIRAK_NOT_FOUND(404, "DOSIRAK002", "도시락 정보가 존재하지 않습니다."),

  /* ORDER ERROR */
  ORDER_DATA_ACCESS_ERROR(500, "ORDER001", "주문 내역 데이터 조회 중 오류가 발생했습니다."),
  ORDER_ITEMS_EMPTY(400, "ORDER002", "주문 항목이 비어 있습니다."),
  ORDER_NOT_FOUND(404, "ORDER003", "주문이 존재하지 않습니다."),
  CANNOT_CANCEL_ORDER(400, "ORDER004", "해당 주문은 취소할 수 없습니다."),
  ORDER_ITEM_NOT_FOUND(404, "ORDER005", "주문 아이템이 존재하지 않습니다.");

  private final int status;
  private final String code;
  private final String message;

  ErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}