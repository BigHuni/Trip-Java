package daehun.trip_java.Cart.Controller;

import daehun.trip_java.Cart.Service.FavoriteService;
import daehun.trip_java.Cart.domain.Favorite;
import daehun.trip_java.User.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {
  private final FavoriteService favoriteService;

  // 여행 즐겨찾기 추가
  @PostMapping("/{tripId}")
  public ResponseEntity<Favorite> addFavorite(@AuthenticationPrincipal User user, @PathVariable Long tripId) {
    Favorite favorite = favoriteService.addFavorite(user, tripId);
    return ResponseEntity.ok(favorite);
  }

  // 사용자의 즐겨찾기 목록 조회
  @GetMapping
  public ResponseEntity<List<Favorite>> getFavorites(@AuthenticationPrincipal User user) {
    List<Favorite> favorites = favoriteService.getFavoritesByUser(user);
    return ResponseEntity.ok(favorites);
  }

  // 여행 즐겨찾기 제거
  @DeleteMapping("/{tripId}")
  public ResponseEntity<Void> removeFavorite(@AuthenticationPrincipal User user, @PathVariable Long tripId) {
    favoriteService.removeFavorite(user, tripId);
    return ResponseEntity.noContent().build();
  }

  // 즐겨찾기 여부 확인
  @GetMapping("/{tripId}/exists")
  public ResponseEntity<Boolean> isFavorite(@AuthenticationPrincipal User user, @PathVariable Long tripId) {
    boolean isFavorite = favoriteService.isFavorite(user, tripId);
    return ResponseEntity.ok(isFavorite);
  }
}