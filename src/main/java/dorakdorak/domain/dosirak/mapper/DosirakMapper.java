package dorakdorak.domain.dosirak.mapper;

import dorakdorak.domain.dosirak.dto.response.MyCustomDosirakAmountResponseDto;
import dorakdorak.domain.dosirak.dto.response.MyCustomDosirakResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DosirakMapper {
    List<MyCustomDosirakResponseDto> findCustomDosiraksByMemberId(@Param("memberId") Long memberId);

    List<MyCustomDosirakResponseDto> findCustomDosiraksPreviewByMemberId(@Param("memberId") Long memberId);

    MyCustomDosirakAmountResponseDto countCustomDosiraksByMemberId(@Param("memberId") Long memberId);
}
