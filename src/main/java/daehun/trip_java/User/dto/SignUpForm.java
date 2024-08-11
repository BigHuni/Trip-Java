package daehun.trip_java.User.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class SignUpForm {
  @NotBlank
  @Length(min =2, max = 20, message = "사용자 이름은 2글자 이상 20글자 이하여야 합니다.")
  @Pattern(regexp = "^[a-z0-9]{2,20}$", message = "아이디는 영문자/숫자 조합 2글자 이상 20글자 이하여야 합니다.")
  private String username;

  @NotBlank
  @Length(min = 8, max = 20)
  @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$", message = "비밀번호는 최소 8자 이상 20글자 이하, 하나 이상의 문자, 숫자, 특수 문자를 포함해야 합니다.")
  private String password;

  @NotBlank
  @Length(max = 30, message = "이메일은 30글자 이하여야 합니다.")
  @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
  private String email;

  @NotBlank
  @Length(min = 8, max = 20)
  private String passwordConfirm;
}