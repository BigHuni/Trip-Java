package daehun.trip_java.Trip.service;

import daehun.trip_java.History.Repository.HistoryRepository;
import daehun.trip_java.History.domain.History;
import daehun.trip_java.Search.domain.Place;
import daehun.trip_java.Trip.domain.Trip;
import daehun.trip_java.Trip.repository.TripRepository;
import daehun.trip_java.User.domain.User;
import daehun.trip_java.User.repository.UserRepository;
import daehun.trip_java.Way.DijkstraAlgorithm;
import daehun.trip_java.Way.PlaceGraph;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

  // 최적 경로 계산
  public List<Place> calculateOptimalPath(List<Place> places) {
    PlaceGraph graph = new PlaceGraph();

    // 그래프에 장소 추가 및 연결
    for (Place place : places) {
      graph.addPlace(place);
    }

    for (int i = 0; i < places.size(); i++) {
      for (int j = i + 1; j < places.size(); j++) {
        graph.addConnection(places.get(i), places.get(j));
      }
    }

    // 다익스트라 알고리즘을 통해 최적 경로 계산
    DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();
    Map<Place, Double> shortestPaths = dijkstra.calculateShortestPath(graph, places.get(0));

    // 최적 경로를 리스트로 반환 (가장 짧은 순으로 정렬)
    return shortestPaths.entrySet().stream()
        .sorted(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
  }
}