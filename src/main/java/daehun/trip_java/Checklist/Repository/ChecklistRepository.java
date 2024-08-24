package daehun.trip_java.Checklist.Repository;

import daehun.trip_java.Checklist.domain.Checklist;
import daehun.trip_java.Trip.domain.Trip;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
  List<Checklist> findByTrip(Trip trip);
}