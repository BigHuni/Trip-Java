package daehun.trip_java.User.service;

import daehun.trip_java.User.domain.User;
import daehun.trip_java.User.dto.UserDTO;
import daehun.trip_java.User.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  // 생성자 주입
  @Autowired
  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  // 회원가입 정보 저장
  public User save(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());
    return userRepository.save(user);
  }

  // 사용자 조회
  public UserDTO findByUsername(String username) {
    Optional<User> optionalUser = userRepository.findByUsername(username);
    User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    return toUserDTO(user);
  }

  // UserDTO
  private UserDTO toUserDTO(User user) {
    return UserDTO.builder()
        .userId(user.getUserId())
        .username(user.getUsername())
        .email(user.getEmail())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .build();
  }

  // 사용자 인증
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDTO userDTO = findByUsername(username);

    return org.springframework.security.core.userdetails.User.withUsername(userDTO.getUsername())
        .password(userDTO.getPassword())
        .roles("USER")
        .build();
  }
}