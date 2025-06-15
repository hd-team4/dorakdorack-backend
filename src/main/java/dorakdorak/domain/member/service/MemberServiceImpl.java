package dorakdorak.domain.member.service;

import dorakdorak.domain.auth.dto.response.MemberAuthDto;
import dorakdorak.domain.dosirak.dto.response.MyCustomDosirakAmountResponseDto;
import dorakdorak.domain.dosirak.mapper.DosirakMapper;
import dorakdorak.domain.member.dto.MemberSignupDto;
import dorakdorak.domain.member.dto.response.MemberSummaryResponseDto;
import dorakdorak.domain.member.dto.response.MyPageSummaryResponse;
import dorakdorak.domain.member.mapper.MemberMapper;
import dorakdorak.domain.order.dto.response.MyOrderAmountResponseDto;
import dorakdorak.domain.order.mapper.OrderMapper;
import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

  private final MemberMapper memberMapper;
  private final DosirakMapper dosirakMapper;
  private final OrderMapper orderMapper;
  private final PasswordEncoder passwordEncoder;


  @Override
  @Transactional
  public void joinMember(MemberSignupDto memberSignupDto) {
    memberSignupDto.setPassword(passwordEncoder.encode(memberSignupDto.getPassword()));
    memberMapper.insertMember(memberSignupDto);
    long joinMemberId = memberSignupDto.getId();
    for (long alleryId : memberSignupDto.getAllergyIds()) {
      memberMapper.insertAllergyCategoryMap(joinMemberId, alleryId);
    }
  }

  @Override
  public Boolean existByEmail(String email) {
    return memberMapper.existByEmail(email);
  }

  @Override
  public MemberAuthDto getMemberAuthInfoByEmail(String email) {

    MemberAuthDto memberAuthDto = memberMapper.findByEmailIntoAuth(email)
        .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

    return memberAuthDto;
  }

  @Override
  @Transactional
  public void updateMemberRefreshToken(String email, String refreshToken) {
    memberMapper.updateMemberRefreshToken(email, refreshToken);
  }

  @Override
  public int findMemberByMemberEmail(String email) {
    return memberMapper.findMemberByMemberEmail(email);
  }

  @Override
  public List<String> findAllergyCategoryNameByMemberId(Long memberId) {
    return memberMapper.findAllergyCategoryNameByMemberId(memberId);
  }

  @Override
  public MyPageSummaryResponse getMyPageSummary(Long memberId) {
    MemberSummaryResponseDto memberSummary = memberMapper.findMemberSummaryByMemberId(memberId);
    if (memberSummary == null) {
      throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
    }

    MyCustomDosirakAmountResponseDto customDosirakAmount = dosirakMapper.countCustomDosiraksByMemberId(
        memberId);
    if (customDosirakAmount == null) {
      throw new BusinessException(ErrorCode.DOSIRAK_DATA_ACCESS_ERROR);
    }

    MyOrderAmountResponseDto normalOrderAmount = orderMapper.countNormalOrdersByMemberId(memberId);
    if (normalOrderAmount == null) {
      throw new BusinessException(ErrorCode.ORDER_DATA_ACCESS_ERROR);
    }

    MyOrderAmountResponseDto groupOrderAmount = orderMapper.countGroupOrdersByMemberId(memberId);
    if (groupOrderAmount == null) {
      throw new BusinessException(ErrorCode.ORDER_DATA_ACCESS_ERROR);
    }

    return new MyPageSummaryResponse(memberSummary.getName(), memberSummary.getEmail(),
        normalOrderAmount.getOrderAmount(), groupOrderAmount.getOrderAmount(),
        customDosirakAmount.getDosirakAmount());
  }
}
