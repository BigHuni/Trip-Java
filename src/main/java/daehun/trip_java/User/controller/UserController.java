package daehun.trip_java.User.controller;

import daehun.trip_java.User.domain.User;
import daehun.trip_java.User.dto.SignUpForm;
import daehun.trip_java.User.service.UserService;
import daehun.trip_java.User.validator.SignUpFormValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

// 사용자 관련 요청 처리 컨트롤러
@Controller
@RequiredArgsConstructor
public class UserController {
  private final SignUpFormValidator signUpFormValidator;
  private final UserService userService;

  // 회원가입 폼 초기화 시, Validator 추가하여 폼 데이터 유효성 검증
  @InitBinder("signUpForm")
  public void signUpFormInitBinder(WebDataBinder webDataBinder) {
    webDataBinder.addValidators(signUpFormValidator);
  }

  // 회원가입 폼 사용자 제공
  @GetMapping("/register")
  public String showRegisterForm(@ModelAttribute SignUpForm signUpForm) {
    return "register";
  }

  // 회원가입 폼 제출 후 데이터 처리
  @PostMapping("/register")
  public String registerUser(@Valid @ModelAttribute SignUpForm signUpForm, Errors errors) {
    if (errors.hasErrors()) {
      return "register";
    }

    // SignUpForm DTO를 User 엔티티로 변환
    User user = new User();
    user.setUsername(signUpForm.getUsername());
    user.setPassword(signUpForm.getPassword());
    user.setEmail(signUpForm.getEmail());

    userService.save(user);
    return "redirect:/login";
  }

  // 로그인 폼 사용자 제공
  @GetMapping("/login")
  public String showLoginForm() {
    return "login";
  }
}