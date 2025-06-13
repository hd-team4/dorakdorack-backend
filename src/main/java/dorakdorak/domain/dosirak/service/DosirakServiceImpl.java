package dorakdorak.domain.dosirak.service;

import dorakdorak.domain.dosirak.dto.response.DosirakDetailImageResponseDto;
import dorakdorak.domain.dosirak.dto.response.DosirakDetailResponse;
import dorakdorak.domain.dosirak.dto.response.DosirakListResponse;
import dorakdorak.domain.dosirak.dto.response.DosirakNutritionResponseDto;
import dorakdorak.domain.dosirak.dto.response.DosirakResponseDto;
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
  public DosirakListResponse getDosiraks(Long dosirakId, FilterType filterType,
      SortType sortType, DosirakType dosirakType, Long count) {

    List<DosirakResponseDto> dosiraks;

    switch (sortType) {
      case LATEST:
        dosiraks = dosirakMapper.findDosiraksOrderByCreatedAt(dosirakId, filterType.name(),
            dosirakType.name(), count);
        break;
      case POPULAR:
        if (dosirakType == DosirakType.NORMAL) {
          dosiraks = dosirakMapper.findNormalDosiraksOrderByPopularity(dosirakId, filterType.name(),
              count);
        } else if (dosirakType == DosirakType.CUSTOM) {
          dosiraks = dosirakMapper.findCustomDosiraksOrderByPopularity(dosirakId, filterType.name(),
              count);
        } else {
          throw new BusinessException(ErrorCode.INVALID_FILTER_ERROR);
        }
        break;
      case PRICE_ASC:
        dosiraks = dosirakMapper.findDosiraksOrderByPriceAsc(dosirakId, filterType.name(),
            dosirakType.name(), count);
        break;
      case PRICE_DESC:
        dosiraks = dosirakMapper.findDosiraksOrderByPriceDesc(dosirakId, filterType.name(),
            dosirakType.name(), count);
        break;
      default:
        throw new BusinessException(ErrorCode.INVALID_FILTER_ERROR);
    }

    if (dosiraks == null) {
      throw new BusinessException(ErrorCode.DOSIRAK_DATA_ACCESS_ERROR);
    }

    return new DosirakListResponse(dosiraks);
  }

  @Override
  public DosirakDetailResponse getDosirakDetail(Long dosirakId) {
    DosirakDetailResponse response = dosirakMapper.findDosirakDetail(dosirakId);
    if (response == null) {
      throw new BusinessException(ErrorCode.DOSIRAK_DATA_ACCESS_ERROR);
    }

    List<DosirakDetailImageResponseDto> detailImages = dosirakMapper.findDetailImages(dosirakId);
    DosirakNutritionResponseDto nutrition = dosirakMapper.findNutrition(dosirakId);

    response.setDetailImages(detailImages);
    response.setNutrition(nutrition);

    return response;
  }

}
