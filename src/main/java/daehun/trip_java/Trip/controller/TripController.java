package daehun.trip_java.Trip.controller;

import daehun.trip_java.History.domain.History;
import daehun.trip_java.Trip.domain.Trip;
import daehun.trip_java.Trip.service.TripService;
import daehun.trip_java.User.domain.User;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripController {

  private final TripService tripService;

  @GetMapping
  public String getTrips(Model model, @AuthenticationPrincipal User user) {
    List<Trip> trips = tripService.getTripsByUsername(user.getUsername());
    model.addAttribute("trips", trips);
    return "tripList";
  }

  @GetMapping("/new")
  public String newTripForm(Model model) {
    model.addAttribute("trip", new Trip());
    model.addAttribute("histories", new ArrayList<History>());
    return "newTrip";
  }

  @PostMapping
  public String createTrip(@AuthenticationPrincipal User user, @ModelAttribute Trip trip, @RequestParam List<History> histories) {
    tripService.createTrip(user.getUsername(), trip.getTripName(), trip.getStartDate(), trip.getEndDate(), histories);
    return "redirect:/trips";
  }

  @GetMapping("/{tripId}/histories")
  public String getHistories(@PathVariable Long tripId, Model model) {
    List<History> histories = tripService.getHistoriesByTrip(tripId);
    model.addAttribute("histories", histories);
    return "historyList";
  }

  @PostMapping("/{tripId}/histories")
  public String addHistory(@PathVariable Long tripId, @ModelAttribute History history) {
    tripService.addHistoryToTrip(tripId, history);
    return "redirect:/trips/" + tripId + "/histories";
  }

  @PostMapping("/histories/{historyId}/sequence")
  public String updateHistorySequence(@PathVariable Long historyId, @RequestParam int newSequence) {
    tripService.updateHistorySequence(historyId, newSequence);
    return "redirect:/trips/histories";
  }
}