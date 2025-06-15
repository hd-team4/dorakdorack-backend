package dorakdorak.domain.auth.service;

import dorakdorak.domain.auth.dto.response.CustomMemberDetails;
import dorakdorak.domain.auth.dto.response.MemberAuthDto;
import dorakdorak.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {

  private static final Logger log = LoggerFactory.getLogger(CustomMemberDetailsService.class);
  private final MemberService memberService;


  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    //DB에서 조회
    MemberAuthDto memberAuthDto = memberService.getMemberAuthInfoByEmail(email);

    if (memberAuthDto != null) {
      //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
      return new CustomMemberDetails(memberAuthDto);
    }

    return null;
  }

}
