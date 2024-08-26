package daehun.trip_java.User.security;

import daehun.trip_java.User.repository.UserRepository;
import daehun.trip_java.User.service.CustomOAuth2UserService;
import daehun.trip_java.User.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;

// 사용자 인증 및 권한 부여, 비밀번호 인코딩 등
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final UserService userService;
  private final UserRepository userRepository;

  // 생성자 주입
  // 의존성 주입 지연(Lazy) 시켜 순환 의존성 해결
  public SecurityConfig(@Lazy UserService userService, UserRepository userRepository) {
    this.userService = userService;
    this.userRepository = userRepository;
  }

  // 해시함수로 사용자 비밀번호 인코딩
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // 보안 필터 체인 정의 -> HttpSecurity 객체 보안 설정
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authorizeRequests ->
            authorizeRequests
                .anyRequest().permitAll() // 모든 요청에 대해 접근 허용
        )
        .formLogin(formLogin ->                 // 로그인 관련 설정
            formLogin
                .loginPage("/login")
                .defaultSuccessUrl("/home")
                .permitAll()
        )
        .oauth2Login(oauth2Login ->             // OAuth2 로그인 관련 설정
            oauth2Login
                .loginPage("/login")
                .defaultSuccessUrl("/home")
                .userInfoEndpoint(userInfoEndpoint ->
                    userInfoEndpoint.userService(oAuth2UserService())
                )
        )
        .logout(logout ->                       // 로그아웃 관련 설정
            logout.permitAll()
        );

    return http.build();
  }

  @Bean
  public DefaultOAuth2UserService oAuth2UserService() {
    return new CustomOAuth2UserService(userRepository);
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