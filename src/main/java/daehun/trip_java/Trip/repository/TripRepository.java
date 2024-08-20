package daehun.trip_java.Trip.repository;

import daehun.trip_java.Trip.domain.Trip;
import daehun.trip_java.User.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long > {
  List<Trip> findByUser(User user);
}
