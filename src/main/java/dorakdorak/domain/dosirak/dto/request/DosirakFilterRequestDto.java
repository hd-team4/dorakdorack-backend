package dorakdorak.domain.dosirak.dto.request;

import dorakdorak.domain.dosirak.enums.FilterType;
import dorakdorak.domain.dosirak.enums.SortType;

public class DosirakFilterRequestDto {
    Long dosirakId;
    FilterType filterType;
    SortType sortType;
}
