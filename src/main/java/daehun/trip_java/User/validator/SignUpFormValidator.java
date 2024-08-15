package daehun.trip_java.User.validator;

import daehun.trip_java.User.dto.SignUpForm;
import daehun.trip_java.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

// 회원가입 폼 검증 -> SignUpForm DTO 검증해서 사용자 중복 체크 및 비밀번호 일치 여부 확인
@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {
  private final UserRepository userRepository;

  @Override
  public boolean supports(Class<?> clazz) {
    return SignUpForm.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    SignUpForm signUpForm = (SignUpForm) target;

    // 사용자 중복 체크
    if (userRepository.existsByUsername(signUpForm.getUsername())) {
      errors.rejectValue("username", "duplicate.username", "이미 사용중인 아이디입니다.");
    }

    // 비밀번호 일치 여부 확인
    if (!signUpForm.getPassword().equals(signUpForm.getPasswordConfirm())) {
      errors.rejectValue("passwordConfirm", "password.mismatch", "비밀번호가 일치하지 않습니다.");
    }
  }
}