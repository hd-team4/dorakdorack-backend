package dorakdorak.domain.dosirak.mapper;

import dorakdorak.domain.dosirak.dto.CustomDosirakRankingDto;
import dorakdorak.domain.dosirak.dto.CustomDosirakSaveDto;
import dorakdorak.domain.dosirak.dto.DosirakDetailImageDto;
import dorakdorak.domain.dosirak.dto.NutritionDto;
import dorakdorak.domain.dosirak.dto.response.DosirakDetailResponse;
import dorakdorak.domain.dosirak.dto.DosirakFilterDto;
import dorakdorak.domain.dosirak.dto.DosirakOrderDto;
import dorakdorak.domain.dosirak.dto.MyCustomDosirakAmountDto;
import dorakdorak.domain.dosirak.dto.MyCustomDosirakDto;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DosirakMapper {

  List<MyCustomDosirakDto> findCustomDosiraksByMemberId(@Param("memberId") Long memberId);

  List<MyCustomDosirakDto> findCustomDosiraksPreviewByMemberId(
      @Param("memberId") Long memberId);

  Optional<MyCustomDosirakAmountDto> countCustomDosiraksByMemberId(@Param("memberId") Long memberId);

  List<DosirakFilterDto> findNormalDosiraksOrderByCreatedAt(
      @Param("dosirakId") Long dosirakId,
      @Param("filterType") String filterType,
      @Param("dosirakType") String dosirakType,
      @Param("count") Long count);

  List<DosirakFilterDto> findCustomDosiraksOrderByCreatedAt(
      @Param("dosirakId") Long dosirakId,
      @Param("filterType") String filterType,
      @Param("dosirakType") String dosirakType,
      @Param("count") Long count);

  List<DosirakFilterDto> findNormalDosiraksOrderByPopularity(
      @Param("dosirakId") Long dosirakId,
      @Param("filterType") String filterType,
      @Param("count") Long count);

  List<DosirakFilterDto> findCustomDosiraksOrderByPopularity(
      @Param("dosirakId") Long dosirakId,
      @Param("filterType") String filterType,
      @Param("count") Long count);

  List<DosirakFilterDto> findNormalDosiraksOrderByPriceAsc(
      @Param("dosirakId") Long dosirakId,
      @Param("filterType") String filterType,
      @Param("dosirakType") String dosirakType,
      @Param("count") Long count);

  List<DosirakFilterDto> findCustomDosiraksOrderByPriceAsc(
      @Param("dosirakId") Long dosirakId,
      @Param("filterType") String filterType,
      @Param("dosirakType") String dosirakType,
      @Param("count") Long count);

  List<DosirakFilterDto> findNormalDosiraksOrderByPriceDesc(
      @Param("dosirakId") Long dosirakId,
      @Param("filterType") String filterType,
      @Param("dosirakType") String dosirakType,
      @Param("count") Long count);

  List<DosirakFilterDto> findCustomDosiraksOrderByPriceDesc(
      @Param("dosirakId") Long dosirakId,
      @Param("filterType") String filterType,
      @Param("dosirakType") String dosirakType,
      @Param("count") Long count);

  Optional<DosirakDetailResponse> findDosirakDetail(@Param("dosirakId") Long dosirakId);

  List<DosirakDetailImageDto> findDetailImages(@Param("dosirakId") Long dosirakId);

  Optional<NutritionDto> findNutrition(@Param("dosirakId") Long dosirakId);

  Optional<DosirakOrderDto> findDosirakOrderDtoById(@Param("dosirakId") Long dosirakId);

  void insertCustomDosirak(CustomDosirakSaveDto customDosirakSaveDto);

  void insertDosirakImage(@Param("imageUrl") String imageUrl,
      @Param("dosirakId") Long dosirakId,
      @Param("createdBy") Long createdBy);

  List<Long> findCategoryIdsByNames(@Param("names") List<String> names);

  void insertDosirakCategoryMap(@Param("dosirakId") Long dosirakId,
      @Param("dosirakCategoryId") Long dosirakCategoryId, @Param("memberId") Long memberId);

  void insertCustomDosirakVote(@Param("dosirakId") Long dosirakId,
      @Param("memberId") Long memberId);

}
