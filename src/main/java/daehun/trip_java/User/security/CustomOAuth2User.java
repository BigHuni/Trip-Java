package daehun.trip_java.User.security;

import daehun.trip_java.User.domain.User;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

  private final User user;
  private final Map<String, Object> attributes;

  public CustomOAuth2User(User user, Map<String, Object> attributes) {
    this.user = user;
    this.attributes = attributes;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));  // 기본적으로 USER 권한 부여
  }

  @Override
  public String getName() {
    return user.getUsername();    // 사용자 이름 반환
  }

}
