package daehun.trip_java.Trip.controller;

import daehun.trip_java.History.domain.History;
import daehun.trip_java.Search.domain.Place;
import daehun.trip_java.Search.service.PlaceService;
import daehun.trip_java.Trip.domain.Trip;
import daehun.trip_java.Trip.dto.TripRequest;
import daehun.trip_java.Trip.service.TripService;
import daehun.trip_java.User.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripController {

  private final TripService tripService;
  private final PlaceService placeService;

  @GetMapping
  public ResponseEntity<List<Trip>> getTrips(@AuthenticationPrincipal User user) {
    List<Trip> trips = tripService.getTripsByUsername(user.getUsername());
    return ResponseEntity.ok(trips);
  }

  @PostMapping
  public ResponseEntity<Trip> createTrip(@AuthenticationPrincipal User user, @RequestBody TripRequest tripRequest) {
    Trip trip = tripRequest.getTrip();
    List<String> placeIds = tripRequest.getPlaceIds();

    List<History> histories = placeIds.stream()
        .map(placeId -> {
          History history = new History();
          history.setPlaceId(placeId);

          // Place 정보를 ES에서 가져와서 History에 설정
          Place place = placeService.getPlaceById(placeId)
              .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 장소 ID입니다: " + placeId));
          history.setPlace(place);

          return history;
        })
        .collect(Collectors.toList());

    tripService.createTrip(user.getUsername(), trip.getTripName(), trip.getStartDate(), trip.getEndDate(), histories);
    return ResponseEntity.ok(trip);
  }

  @GetMapping("/{tripId}/histories")
  public ResponseEntity<List<History>> getHistories(@PathVariable Long tripId) {
    List<History> histories = tripService.getHistoriesByTrip(tripId);
    return ResponseEntity.ok(histories);
  }

  @PostMapping("/{tripId}/histories")
  public ResponseEntity<History> addHistory(@PathVariable Long tripId, @RequestBody History history) {
    tripService.addHistoryToTrip(tripId, history);
    return ResponseEntity.ok(history);
  }

  @PostMapping("/histories/{historyId}/sequence")
  public ResponseEntity<Void> updateHistorySequence(@PathVariable Long historyId, @RequestParam int newSequence) {
    tripService.updateHistorySequence(historyId, newSequence);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{tripId}/calculate-optimal-path")
  public ResponseEntity<List<Place>> calculateOptimalPath(@PathVariable Long tripId) {
    List<History> histories = tripService.getHistoriesByTrip(tripId);
    List<Place> places = histories.stream()
        .map(history -> placeService.getPlaceById(history.getPlaceId()).orElse(null))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    List<Place> optimalPath = tripService.calculateOptimalPath(places);

    return ResponseEntity.ok(optimalPath);
  }
}