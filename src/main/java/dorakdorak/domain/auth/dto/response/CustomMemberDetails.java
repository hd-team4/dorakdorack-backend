package dorakdorak.domain.auth.dto.response;

import java.util.ArrayList;
import java.util.Collection;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@ToString
public class CustomMemberDetails implements UserDetails {

  private final MemberAuthDto memberAuthdto;

  public CustomMemberDetails(MemberAuthDto memberAuthdto) {

    this.memberAuthdto = memberAuthdto;
  }


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    Collection<GrantedAuthority> collection = new ArrayList<>();

    collection.add(new GrantedAuthority() {

      @Override
      public String getAuthority() {

        return memberAuthdto.getRole();
      }
    });

    return collection;
  }

  public Long getId() {
    return memberAuthdto.getId();
  }

  public Long getUid() {
    return memberAuthdto.getUniversityId();
  }

  public String getRole() {
    return memberAuthdto.getRole();
  }

  @Override
  public String getPassword() {

    return memberAuthdto.getPassword();
  }

  @Override
  public String getUsername() {
    return memberAuthdto.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {

    return true;
  }

  @Override
  public boolean isAccountNonLocked() {

    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {

    return true;
  }

  @Override
  public boolean isEnabled() {

    return true;
  }
}
