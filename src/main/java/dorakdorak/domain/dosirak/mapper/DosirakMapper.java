package dorakdorak.domain.dosirak.mapper;

import dorakdorak.domain.dosirak.dto.response.DosirakDetailImageResponseDto;
import dorakdorak.domain.dosirak.dto.response.DosirakDetailResponse;
import dorakdorak.domain.dosirak.dto.response.DosirakFilterResponseDto;
import dorakdorak.domain.dosirak.dto.response.DosirakNutritionResponseDto;
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

  List<DosirakFilterResponseDto> findNormalDosiraksOrderByCreatedAt(Long dosirakId,
      String filterType,
      String dosirakType,
      Long count);

  List<DosirakFilterResponseDto> findCustomDosiraksOrderByCreatedAt(Long dosirakId,
      String filterType,
      String dosirakType,
      Long count);

  List<DosirakFilterResponseDto> findNormalDosiraksOrderByPopularity(Long dosirakId,
      String filterType,
      Long count);

  List<DosirakFilterResponseDto> findCustomDosiraksOrderByPopularity(Long dosirakId,
      String filterType,
      Long count);

  List<DosirakFilterResponseDto> findNormalDosiraksOrderByPriceAsc(Long dosirakId,
      String filterType,
      String dosirakType,
      Long count);

  List<DosirakFilterResponseDto> findCustomDosiraksOrderByPriceAsc(Long dosirakId,
      String filterType,
      String dosirakType,
      Long count);

  List<DosirakFilterResponseDto> findNormalDosiraksOrderByPriceDesc(Long dosirakId,
      String filterType,
      String dosirakType,
      Long count);

  List<DosirakFilterResponseDto> findCustomDosiraksOrderByPriceDesc(Long dosirakId,
      String filterType,
      String dosirakType,
      Long count);

  DosirakDetailResponse findDosirakDetail(Long dosirakId);

  List<DosirakDetailImageResponseDto> findDetailImages(Long dosirakId);

  DosirakNutritionResponseDto findNutrition(Long dosirakId);
}
