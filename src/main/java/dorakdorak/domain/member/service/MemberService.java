package dorakdorak.domain.member.service;

import dorakdorak.domain.member.dto.request.MemberSignupRequest;

public interface MemberService {

  void joinMember(MemberSignupRequest memberSignupRequest);

  int findMemberByMemberEmail(String email);
}
