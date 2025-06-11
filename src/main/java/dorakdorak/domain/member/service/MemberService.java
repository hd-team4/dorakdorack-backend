package dorakdorak.domain.member.service;

import dorakdorak.domain.auth.dto.response.MemberAuthDto;
import dorakdorak.domain.member.dto.request.MemberSignupRequest;

public interface MemberService {

  void joinMember(MemberSignupRequest memberSignupRequest);

  Boolean existByEmail(String email);

  MemberAuthDto findByEmailIntoAuth(String email);

  void updateMemberRefreshToken(String email, String refreshToken);

  int findMemberByMemberEmail(String email);
}
