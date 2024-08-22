package daehun.trip_java.Cart.Repository;

import daehun.trip_java.Cart.domain.Favorite;
import daehun.trip_java.Trip.domain.Trip;
import daehun.trip_java.User.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
  List<Favorite> findByUser(User user);
  Optional<Favorite> findByUserAndTrip(User user, Trip trip);
  void deleteByUserAndTrip(User user, Trip trip);
  boolean existsByUserAndTrip(User user, Trip trip);
}