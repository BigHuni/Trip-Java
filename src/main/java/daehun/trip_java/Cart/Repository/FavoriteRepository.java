package daehun.trip_java.Cart.Repository;

import daehun.trip_java.Cart.domain.Favorite;
import daehun.trip_java.Trip.domain.Trip;
import daehun.trip_java.User.domain.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
  Page<Favorite> findByUser(User user, Pageable pageable);
  Optional<Favorite> findByUserAndTrip(User user, Trip trip);
  void deleteByUserAndTrip(User user, Trip trip);
  boolean existsByUserAndTrip(User user, Trip trip);
}