package daehun.trip_java.User.service;

import daehun.trip_java.User.domain.User;
import daehun.trip_java.User.repository.UserRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  // 회원가입 정보 저장
  public User save(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setCreated_at(LocalDateTime.now());
    user.setUpdated_at(LocalDateTime.now());
    return userRepository.save(user);
  }

  // 사용자 조회
  public User findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  // 사용자 인증
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("사용자를 찾지 못했습니다.");
    }

    return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
        .password(user.getPassword())
        .roles("USER")
        .build();
  }
}