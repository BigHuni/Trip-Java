package daehun.trip_java.User.service;

import daehun.trip_java.User.domain.User;
import daehun.trip_java.User.repository.UserRepository;
import daehun.trip_java.User.security.CustomOAuth2User;
import java.util.Optional;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2UserService extends DefaultOAuth2UserService {
  private final UserRepository userRepository;

  public CustomOAuth2UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);

    String email = oAuth2User.getAttribute("email");
    Optional<User> userOptional = userRepository.findByEmail(email);

    User user;
    if (userOptional.isPresent()) {
      user = userOptional.get();
    } else {
      // 새 사용자 등록
      user = new User();
      user.setUsername(oAuth2User.getAttribute("name"));
      user.setEmail(email);
      userRepository.save(user);
    }

    return new CustomOAuth2User(user, oAuth2User.getAttributes());
  }
}
