package daehun.trip_java.Cart.Repository;

import daehun.trip_java.Cart.domain.Cart;
import daehun.trip_java.User.domain.User;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {
  List<Cart> findAllByUser(User user);
  void deleteByUserAndPlaceId(User user, Long placeId);

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM cart WHERE user_id = :userId", nativeQuery = true)
  void deleteAllByUserId(@Param("userId") Long userId);
}