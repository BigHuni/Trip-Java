package daehun.trip_java.User.service;

import daehun.trip_java.User.domain.User;
import daehun.trip_java.User.dto.UserDTO;
import daehun.trip_java.User.repository.UserRepository;
import daehun.trip_java.User.security.CustomOAuth2User;
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
    User user = userRepository.findByEmail(email)
        .orElseGet(() -> registerNewUser(oAuth2User));

    UserDTO userDTO = toUserDTO(user);

    return new CustomOAuth2User(userDTO, oAuth2User.getAttributes());
  }

  private User registerNewUser(OAuth2User oAuth2User) {
    User newUser = new User();
    newUser.setUsername(oAuth2User.getAttribute("name"));
    newUser.setEmail(oAuth2User.getAttribute("email"));
    return userRepository.save(newUser);
  }

  private UserDTO toUserDTO(User user) {
    return UserDTO.builder()
        .userId(user.getUserId())
        .username(user.getUsername())
        .email(user.getEmail())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .build();
  }
}
