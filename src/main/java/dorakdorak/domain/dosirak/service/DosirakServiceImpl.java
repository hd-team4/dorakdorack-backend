package dorakdorak.domain.dosirak.service;

import dorakdorak.domain.dosirak.dto.response.DosirakListResponse;
import dorakdorak.domain.dosirak.dto.response.DosirakResponseDto;
import dorakdorak.domain.dosirak.dto.response.MyCustomDosirakResponseDto;
import dorakdorak.domain.dosirak.dto.response.MyCustomDosirakResponse;
import dorakdorak.domain.dosirak.enums.FilterType;
import dorakdorak.domain.dosirak.enums.SortType;
import dorakdorak.domain.dosirak.mapper.DosirakMapper;
import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DosirakServiceImpl implements DosirakService{

    private final DosirakMapper dosirakMapper;

    @Override
    public MyCustomDosirakResponse getCustomDosiraksByMemberId(Long memberId) {
        List<MyCustomDosirakResponseDto> myCustomDosiraks = dosirakMapper.findCustomDosiraksByMemberId(memberId);

        if (myCustomDosiraks == null) {
            throw new BusinessException(ErrorCode.DOSIRAK_DATA_ACCESS_ERROR);
        }

        return new MyCustomDosirakResponse(myCustomDosiraks);
    }

    @Override
    public MyCustomDosirakResponse getCustomDosiraksPreviewByMemberId(Long memberId) {
        List<MyCustomDosirakResponseDto> myCustomDosiraks = dosirakMapper.findCustomDosiraksPreviewByMemberId(memberId);

        if (myCustomDosiraks == null) {
            throw new BusinessException(ErrorCode.DOSIRAK_DATA_ACCESS_ERROR);
        }

        return new MyCustomDosirakResponse(myCustomDosiraks);
    }

    @Override
    public DosirakListResponse getDosiraks(Long dosirakId, FilterType filterType,
        SortType sortType) {
        List<DosirakResponseDto> dosiraks = dosirakMapper.findDosiraks(dosirakId, filterType.name(),
            sortType.name());

        if (dosiraks == null) {
            throw new BusinessException(ErrorCode.DOSIRAK_DATA_ACCESS_ERROR);
        }

        return new DosirakListResponse(dosiraks);
    }
}
