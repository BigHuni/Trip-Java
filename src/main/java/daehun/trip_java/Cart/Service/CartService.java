package daehun.trip_java.Cart.Service;

import daehun.trip_java.Cart.Repository.CartRepository;
import daehun.trip_java.Cart.domain.Cart;
import daehun.trip_java.Search.service.PlaceService;
import daehun.trip_java.User.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
  private final CartRepository cartRepository;
  private final PlaceService placeService;

  // 사용자가 여행지를 장바구니에 추가
  public Cart addToCart(User user, Long placeId) {

    // Place 존재 여부 확인
    if (placeService.getPlaceById(placeId).isEmpty()) {
      throw new IllegalArgumentException("유효하지 않은 여행지입니다.");
    }

    Cart cart = new Cart();
    cart.setUser(user);
    cart.setPlaceId(placeId);
    return cartRepository.save(cart);
  }

  // 특정 사용자의 모든 장바구니 항목 조회
  public List<Cart> getCartItemsByUser(User user) {
    return cartRepository.findAllByUser(user);
  }

  // 사용자의 장바구니에서 특정 여행지 제거
  public void removeFromCart(User user, Long placeId) {
    cartRepository.deleteByUserAndPlaceId(user, placeId);
  }

  // 사용자의 모든 장바구니 항목 제거
  public void clearCart(User user) {
    cartRepository.deleteAllByUserId(user.getUserId());
  }
}