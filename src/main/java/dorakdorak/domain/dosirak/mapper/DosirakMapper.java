package dorakdorak.domain.dosirak.mapper;

import dorakdorak.domain.dosirak.dto.CustomDosirakSaveDto;
import dorakdorak.domain.dosirak.dto.response.DosirakDetailImageResponseDto;
import dorakdorak.domain.dosirak.dto.response.DosirakDetailResponse;
import dorakdorak.domain.dosirak.dto.response.DosirakFilterResponseDto;
import dorakdorak.domain.dosirak.dto.response.DosirakNutritionResponseDto;
import dorakdorak.domain.dosirak.dto.response.DosirakOrderDto;
import dorakdorak.domain.dosirak.dto.response.MyCustomDosirakAmountResponseDto;
import dorakdorak.domain.dosirak.dto.response.MyCustomDosirakResponseDto;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DosirakMapper {

  List<MyCustomDosirakResponseDto> findCustomDosiraksByMemberId(@Param("memberId") Long memberId);

  List<MyCustomDosirakResponseDto> findCustomDosiraksPreviewByMemberId(
      @Param("memberId") Long memberId);

  MyCustomDosirakAmountResponseDto countCustomDosiraksByMemberId(@Param("memberId") Long memberId);

  List<DosirakFilterResponseDto> findNormalDosiraksOrderByCreatedAt(
      @Param("dosirakId") Long dosirakId,
      @Param("filterType") String filterType,
      @Param("dosirakType") String dosirakType,
      @Param("count") Long count);

  List<DosirakFilterResponseDto> findCustomDosiraksOrderByCreatedAt(
      @Param("dosirakId") Long dosirakId,
      @Param("filterType") String filterType,
      @Param("dosirakType") String dosirakType,
      @Param("count") Long count);

  List<DosirakFilterResponseDto> findNormalDosiraksOrderByPopularity(
      @Param("dosirakId") Long dosirakId,
      @Param("filterType") String filterType,
      @Param("count") Long count);

  List<DosirakFilterResponseDto> findCustomDosiraksOrderByPopularity(
      @Param("dosirakId") Long dosirakId,
      @Param("filterType") String filterType,
      @Param("count") Long count);

  List<DosirakFilterResponseDto> findNormalDosiraksOrderByPriceAsc(
      @Param("dosirakId") Long dosirakId,
      @Param("filterType") String filterType,
      @Param("dosirakType") String dosirakType,
      @Param("count") Long count);

  List<DosirakFilterResponseDto> findCustomDosiraksOrderByPriceAsc(
      @Param("dosirakId") Long dosirakId,
      @Param("filterType") String filterType,
      @Param("dosirakType") String dosirakType,
      @Param("count") Long count);

  List<DosirakFilterResponseDto> findNormalDosiraksOrderByPriceDesc(
      @Param("dosirakId") Long dosirakId,
      @Param("filterType") String filterType,
      @Param("dosirakType") String dosirakType,
      @Param("count") Long count);

  List<DosirakFilterResponseDto> findCustomDosiraksOrderByPriceDesc(
      @Param("dosirakId") Long dosirakId,
      @Param("filterType") String filterType,
      @Param("dosirakType") String dosirakType,
      @Param("count") Long count);

  DosirakDetailResponse findDosirakDetail(@Param("dosirakId") Long dosirakId);

  List<DosirakDetailImageResponseDto> findDetailImages(@Param("dosirakId") Long dosirakId);

  DosirakNutritionResponseDto findNutrition(@Param("dosirakId") Long dosirakId);

  Optional<DosirakOrderDto> findDosirakOrderDtoById(@Param("dosirakId") Long dosirakId);

  void insertCustomDosirak(CustomDosirakSaveDto customDosirakSaveDto);

  void insertDosirakImage(@Param("imageUrl") String imageUrl,
      @Param("dosirakId") Long dosirakId,
      @Param("createdBy") Long createdBy);

  List<Long> findCategoryIdsByNames(@Param("names") List<String> names);

  void insertDosirakCategoryMap(@Param("dosirakId") Long dosirakId,
      @Param("dosirakCategoryId") Long dosirakCategoryId, @Param("createdBy") Long memberId);

  void insertCustomDosirakVote(@Param("dosirakId") Long dosirakId,
      @Param("memberId") Long memberId);

}
