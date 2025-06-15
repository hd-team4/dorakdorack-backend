package dorakdorak.domain.member.service;

import dorakdorak.domain.auth.dto.response.MemberAuthDto;
import dorakdorak.domain.member.dto.MemberSignupDto;
import dorakdorak.domain.member.dto.response.MyPageSummaryResponse;
import java.util.List;

public interface MemberService {

  void joinMember(MemberSignupDto memberSignupDto);

  Boolean existByEmail(String email);

  MemberAuthDto getMemberAuthInfoByEmail(String email);

  void updateMemberRefreshToken(String email, String refreshToken);

  int findMemberByMemberEmail(String email);

  List<String> findAllergyCategoryNameByMemberId(Long memberId);

  // 마이페이지 상단 요약 정보 조회
  MyPageSummaryResponse getMyPageSummary(Long memberId);
}
