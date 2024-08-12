package daehun.trip_java.User.security;

import daehun.trip_java.User.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 사용자 인증 및 권한 부여, 비밀번호 인코딩 등
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private UserService userService;

  // 해시함수로 사용자 비밀번호 인코딩
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // 보안 필터 체인 정의 -> HttpSecurity 객체 보안 설정
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authenticationProvider(daoAuthenticationProvider())    // 사용자 비밀번호 검증
        .authorizeHttpRequests(authorizeRequests ->             // 요청 접근 권한 설정
            authorizeRequests
                .requestMatchers("/register", "/login", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
        )
        .formLogin(formLogin ->                 // 로그인 관련 설정
            formLogin
                .loginPage("/login")
                .defaultSuccessUrl("/home")
                .permitAll()
        )
        .logout(logout ->                       // 로그아웃 관련 설정
            logout.permitAll()
        );

    return http.build();
  }

  // 사용자 인증 정보 DB 로드 후 비밀번호 검증
  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }
}