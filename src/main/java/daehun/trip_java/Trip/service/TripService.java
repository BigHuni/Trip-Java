package daehun.trip_java.Trip.service;

import daehun.trip_java.History.Repository.HistoryRepository;
import daehun.trip_java.History.domain.History;
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
  private final HistoryRepository historyRepository;

  public Trip createTrip(String username, String tripName, LocalDate startDate, LocalDate endDate, List<History> histories) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));

    Trip trip = new Trip();
    trip.setUser(user);
    trip.setTripName(tripName);
    trip.setStartDate(startDate);
    trip.setEndDate(endDate);
    Trip savedTrip = tripRepository.save(trip);

    // 경유지(History) 저장
    for (History history : histories) {
      history.setTrip(savedTrip);
      historyRepository.save(history);
    }

    return savedTrip;
  }

  public List<Trip> getTripsByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));
    return tripRepository.findByUser(user);
  }

  public List<History> getHistoriesByTrip(Long tripId) {
    Trip trip = tripRepository.findById(tripId)
        .orElseThrow(() -> new IllegalArgumentException("해당 여행을 찾을 수 없습니다."));
    return historyRepository.findByTripOrderBySequence(trip);
  }

  public void addHistoryToTrip(Long tripId, History history) {
    Trip trip = tripRepository.findById(tripId)
        .orElseThrow(() -> new IllegalArgumentException("해당 여행을 찾을 수 없습니다."));
    history.setTrip(trip);
    historyRepository.save(history);
  }

  public void updateHistorySequence(Long historyId, int newSequence) {
    History history = historyRepository.findById(historyId)
        .orElseThrow(() -> new IllegalArgumentException("해당 경유지를 찾을 수 없습니다."));
    history.setSequence(newSequence);
    historyRepository.save(history);
  }
}