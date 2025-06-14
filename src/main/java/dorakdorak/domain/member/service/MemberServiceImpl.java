package dorakdorak.domain.member.service;

import dorakdorak.domain.auth.dto.response.MemberAuthDto;
import dorakdorak.domain.member.dto.request.MemberSignupRequest;
import dorakdorak.domain.member.mapper.MemberMapper;
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
  private final PasswordEncoder passwordEncoder;


  @Override
  @Transactional
  public void joinMember(MemberSignupRequest memberSignupRequest) {
    memberSignupRequest.setPassword(passwordEncoder.encode(memberSignupRequest.getPassword()));
    memberMapper.insertMember(memberSignupRequest);
    long joinMemberId = memberSignupRequest.getId();
    for (long alleryId : memberSignupRequest.getAllergyIds()) {
      memberMapper.insertAllergyCategoryMap(joinMemberId, alleryId);
    }
  }

  @Override
  public Boolean existByEmail(String email) {
    return memberMapper.existByEmail(email);
  }

  @Override
  public MemberAuthDto findByEmailIntoAuth(String email) {
    return memberMapper.findByEmailIntoAuth(email);
  }

  @Override
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
}
