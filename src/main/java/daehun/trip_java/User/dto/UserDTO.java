package daehun.trip_java.User.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
  private Long userId;
  private String username;
  private String password;
  private String email;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}