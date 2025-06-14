package dorakdorak.domain.dosirak.service;

import dorakdorak.domain.dosirak.dto.CustomDosirakSaveDto;
import dorakdorak.domain.dosirak.dto.response.DosirakDetailImageResponseDto;
import dorakdorak.domain.dosirak.dto.response.DosirakDetailResponse;
import dorakdorak.domain.dosirak.dto.response.DosirakFilterResponse;
import dorakdorak.domain.dosirak.dto.response.DosirakFilterResponseDto;
import dorakdorak.domain.dosirak.dto.response.DosirakNutritionResponseDto;
import dorakdorak.domain.dosirak.dto.response.MyCustomDosirakResponse;
import dorakdorak.domain.dosirak.dto.response.MyCustomDosirakResponseDto;
import dorakdorak.domain.dosirak.enums.DosirakType;
import dorakdorak.domain.dosirak.enums.FilterType;
import dorakdorak.domain.dosirak.enums.SortType;
import dorakdorak.domain.dosirak.mapper.DosirakMapper;
import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DosirakServiceImpl implements DosirakService {

  private final DosirakMapper dosirakMapper;

  @Override
  public MyCustomDosirakResponse getCustomDosiraksByMemberId(Long memberId) {
    List<MyCustomDosirakResponseDto> myCustomDosiraks = dosirakMapper.findCustomDosiraksByMemberId(
        memberId);

    if (myCustomDosiraks == null) {
      throw new BusinessException(ErrorCode.DOSIRAK_DATA_ACCESS_ERROR);
    }

    return new MyCustomDosirakResponse(myCustomDosiraks);
  }

  @Override
  public MyCustomDosirakResponse getCustomDosiraksPreviewByMemberId(Long memberId) {
    List<MyCustomDosirakResponseDto> myCustomDosiraks = dosirakMapper.findCustomDosiraksPreviewByMemberId(
        memberId);

    if (myCustomDosiraks == null) {
      throw new BusinessException(ErrorCode.DOSIRAK_DATA_ACCESS_ERROR);
    }

    return new MyCustomDosirakResponse(myCustomDosiraks);
  }

  @Override
  public DosirakFilterResponse getDosiraks(Long dosirakId, FilterType filterType,
      SortType sortType, DosirakType dosirakType, Long count) {

    List<DosirakFilterResponseDto> dosiraks;

    switch (sortType) {
      case LATEST:
        if (dosirakType == DosirakType.NORMAL) {
          dosiraks = dosirakMapper.findNormalDosiraksOrderByCreatedAt(
              dosirakId, filterType.name(), dosirakType.name(), count);
        } else if (dosirakType == DosirakType.CUSTOM) {
          dosiraks = dosirakMapper.findCustomDosiraksOrderByCreatedAt(
              dosirakId, filterType.name(), dosirakType.name(), count);
        } else {
          throw new BusinessException(ErrorCode.INVALID_DOSIRAK_FILTER);
        }
        break;

      case POPULAR:
        if (dosirakType == DosirakType.NORMAL) {
          dosiraks = dosirakMapper.findNormalDosiraksOrderByPopularity(
              dosirakId, filterType.name(), count);
        } else if (dosirakType == DosirakType.CUSTOM) {
          dosiraks = dosirakMapper.findCustomDosiraksOrderByPopularity(
              dosirakId, filterType.name(), count);
        } else {
          throw new BusinessException(ErrorCode.INVALID_DOSIRAK_FILTER);
        }
        break;

      case PRICE_ASC:
        if (dosirakType == DosirakType.NORMAL) {
          dosiraks = dosirakMapper.findNormalDosiraksOrderByPriceAsc(
              dosirakId, filterType.name(), dosirakType.name(), count);
        } else if (dosirakType == DosirakType.CUSTOM) {
          dosiraks = dosirakMapper.findCustomDosiraksOrderByPriceAsc(
              dosirakId, filterType.name(), dosirakType.name(), count);
        } else {
          throw new BusinessException(ErrorCode.INVALID_DOSIRAK_FILTER);
        }
        break;

      case PRICE_DESC:
        if (dosirakType == DosirakType.NORMAL) {
          dosiraks = dosirakMapper.findNormalDosiraksOrderByPriceDesc(
              dosirakId, filterType.name(), dosirakType.name(), count);
        } else if (dosirakType == DosirakType.CUSTOM) {
          dosiraks = dosirakMapper.findCustomDosiraksOrderByPriceDesc(
              dosirakId, filterType.name(), dosirakType.name(), count);
        } else {
          throw new BusinessException(ErrorCode.INVALID_DOSIRAK_FILTER);
        }
        break;

      default:
        throw new BusinessException(ErrorCode.INVALID_DOSIRAK_FILTER);
    }

    if (dosiraks == null) {
      throw new BusinessException(ErrorCode.DOSIRAK_DATA_ACCESS_ERROR);
    }

    return new DosirakFilterResponse(dosiraks);
  }

  @Override
  public DosirakDetailResponse getDosirakDetail(Long dosirakId) {
    DosirakDetailResponse response = dosirakMapper.findDosirakDetail(dosirakId);
    if (response == null) {
      throw new BusinessException(ErrorCode.DOSIRAK_DATA_ACCESS_ERROR);
    }

    List<DosirakDetailImageResponseDto> detailImages = dosirakMapper.findDetailImages(dosirakId);
    if (detailImages == null || detailImages.isEmpty()) {
      throw new BusinessException(ErrorCode.DOSIRAK_IMAGE_NOT_FOUND);
    }

    DosirakNutritionResponseDto nutrition = dosirakMapper.findNutrition(dosirakId);
    if (nutrition == null) {
      throw new BusinessException(ErrorCode.DOSIRAK_NUTRITION_NOT_FOUND);
    }

    response.setDetailImages(detailImages);
    response.setNutrition(nutrition);

    return response;
  }

  @Override
  @Transactional
  public void registerCustomDosirak(CustomDosirakSaveDto customDosirakSaveDto) {

    // 도시락에 등록
    dosirakMapper.insertCustomDosirak(customDosirakSaveDto);

    // 도시락 이미지 등록
    dosirakMapper.insertDosirakImage(customDosirakSaveDto.getImageUrl(),
        customDosirakSaveDto.getId(), customDosirakSaveDto.getMemberId());

    // 도시락 카테고리 이름으로 ID 목록 조회
    List<Long> categoryIds = dosirakMapper.findCategoryIdsByNames(
        customDosirakSaveDto.getCategories());

    // ERRORCODE로 수정
    if (categoryIds.size() != customDosirakSaveDto.getCategories().size()) {
      throw new IllegalArgumentException("존재하지 않는 카테고리가 포함되어 있습니다.");
    }

    // 도시락 카테고리 맵에 등록
    for (Long categoryId : categoryIds) {
      dosirakMapper.insertDosirakCategoryMap(customDosirakSaveDto.getId(), categoryId,
          customDosirakSaveDto.getMemberId());
    }
  }
}
