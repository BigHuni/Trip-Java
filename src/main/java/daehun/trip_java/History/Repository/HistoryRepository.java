package daehun.trip_java.History.Repository;

import daehun.trip_java.History.domain.History;
import daehun.trip_java.Trip.domain.Trip;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
  List<History> findByTripOrderBySequence(Trip trip);
}