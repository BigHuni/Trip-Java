package daehun.trip_java.User.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO {
  private Long userId;
  private String username;
  private String password;
  private String email;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}