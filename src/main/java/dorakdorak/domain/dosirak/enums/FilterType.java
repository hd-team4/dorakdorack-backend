package dorakdorak.domain.dosirak.enums;

import lombok.Getter;

@Getter
public enum FilterType {
  ALL("전체"),
  HIGH_BLOOD_PRESSURE("고혈압 식단"),
  LOW_CALORIE("칼로리 식단"),
  SPECIAL("스페셜 식단"),
  HIGH_PROTEIN("단백질 식단"),
  DIABETIC("당뇨 식단"),
  VALUE("가성비 식단");

  private final String koreanName;

  FilterType(String koreanName) {
    this.koreanName = koreanName;
  }

}
