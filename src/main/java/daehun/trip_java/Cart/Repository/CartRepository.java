package daehun.trip_java.Cart.Repository;

import daehun.trip_java.Cart.domain.Cart;
import daehun.trip_java.User.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
  List<Cart> findByUser(User user);
  void deleteByUserAndPlaceId(User user, Long placeId);
}