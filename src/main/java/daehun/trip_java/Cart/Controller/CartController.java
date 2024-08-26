package daehun.trip_java.Cart.Controller;

import daehun.trip_java.Cart.Service.CartService;
import daehun.trip_java.Cart.domain.Cart;
import daehun.trip_java.User.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
  private final CartService cartService;

  // 장바구니 여행지 추가
  @PostMapping
  public ResponseEntity<Cart> addToCart(@AuthenticationPrincipal User user, @RequestParam String placeId) {
    Cart cart = cartService.addToCart(user, placeId);
    return ResponseEntity.ok(cart);
  }

  // 장바구니 항목 조회
  @GetMapping
  public ResponseEntity<List<Cart>> getCartItems(@AuthenticationPrincipal User user) {
    List<Cart> cartItems = cartService.getCartItemsByUser(user);
    return ResponseEntity.ok(cartItems);
  }

  // 장바구니에서 특정 여행지 제거
  @DeleteMapping
  public ResponseEntity<Void> removeFromCart(@AuthenticationPrincipal User user, @RequestParam String placeId) {
    cartService.removeFromCart(user, placeId);
    return ResponseEntity.noContent().build();
  }

  // 장바구니 비우기
  @DeleteMapping("/clear")
  public ResponseEntity<Void> clearCart(@AuthenticationPrincipal User user) {
    cartService.clearCart(user);
    return ResponseEntity.noContent().build();
  }
}