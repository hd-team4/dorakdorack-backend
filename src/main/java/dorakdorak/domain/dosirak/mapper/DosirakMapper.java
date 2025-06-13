package dorakdorak.domain.dosirak.mapper;

import dorakdorak.domain.dosirak.dto.response.DosirakDetailImageResponseDto;
import dorakdorak.domain.dosirak.dto.response.DosirakDetailResponse;
import dorakdorak.domain.dosirak.dto.response.DosirakNutritionResponseDto;
import dorakdorak.domain.dosirak.dto.response.DosirakResponseDto;
import dorakdorak.domain.dosirak.dto.response.MyCustomDosirakAmountResponseDto;
import dorakdorak.domain.dosirak.dto.response.MyCustomDosirakResponseDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DosirakMapper {

  List<MyCustomDosirakResponseDto> findCustomDosiraksByMemberId(@Param("memberId") Long memberId);

  List<MyCustomDosirakResponseDto> findCustomDosiraksPreviewByMemberId(
      @Param("memberId") Long memberId);

  MyCustomDosirakAmountResponseDto countCustomDosiraksByMemberId(@Param("memberId") Long memberId);

  List<DosirakResponseDto> findDosiraksOrderByCreatedAt(Long dosirakId, String filterType,
      String dosirakType,
      Long count);

  List<DosirakResponseDto> findNormalDosiraksOrderByPopularity(Long dosirakId, String filterType,
      Long count);

  List<DosirakResponseDto> findCustomDosiraksOrderByPopularity(Long dosirakId, String filterType,
      Long count);

  List<DosirakResponseDto> findDosiraksOrderByPriceAsc(Long dosirakId, String filterType,
      String dosirakType,
      Long count);

  List<DosirakResponseDto> findDosiraksOrderByPriceDesc(Long dosirakId, String filterType,
      String dosirakType,
      Long count);

  DosirakDetailResponse findDosirakDetail(Long dosirakId);

  List<DosirakDetailImageResponseDto> findDetailImages(Long dosirakId);

  DosirakNutritionResponseDto findNutrition(Long dosirakId);
}
