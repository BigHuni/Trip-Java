package daehun.trip_java.Trip.service;

import daehun.trip_java.Trip.domain.Trip;
import daehun.trip_java.Trip.repository.TripRepository;
import daehun.trip_java.User.domain.User;
import daehun.trip_java.User.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripService {
  private final TripRepository tripRepository;
  private final UserRepository userRepository;

  public Trip createTrip(String username, String tripName, LocalDate startDate, LocalDate endDate) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾지 못했습니다."));

    Trip trip = new Trip();
    trip.setUser(user);
    trip.setTripName(tripName);
    trip.setStartDate(startDate);
    trip.setEndDate(endDate);
    return tripRepository.save(trip);
  }

  public List<Trip> getTripsByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾기 못했습니다."));
    return tripRepository.findByUser(user);
  }
}