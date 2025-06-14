package dorakdorak.domain.member.service;

import dorakdorak.domain.auth.dto.response.MemberAuthDto;
import dorakdorak.domain.member.dto.request.MemberSignupRequest;
import java.util.List;
import dorakdorak.domain.member.dto.response.MyPageSummaryResponse;

public interface MemberService {

  void joinMember(MemberSignupRequest memberSignupRequest);

  Boolean existByEmail(String email);

  MemberAuthDto findByEmailIntoAuth(String email);

  void updateMemberRefreshToken(String email, String refreshToken);

  int findMemberByMemberEmail(String email);

  List<String> findAllergyCategoryNameByMemberId(Long memberId);

  // 마이페이지 상단 요약 정보 조회
  MyPageSummaryResponse getMyPageSummary(Long memberId);
}
