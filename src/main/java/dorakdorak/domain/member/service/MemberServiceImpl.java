package dorakdorak.domain.member.service;

import dorakdorak.domain.member.dto.request.MemberSignupRequest;
import dorakdorak.domain.member.mapper.MemberMapper;
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

  public boolean verifyPassword(String rawPassword, String encodedPassword) {
    try {
      boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
      log.debug("비밀번호 검증 결과: {}", matches);
      return matches;
    } catch (Exception e) {
      log.error("비밀번호 검증 실패: {}", e.getMessage());
      return false;
    }
  }
}
