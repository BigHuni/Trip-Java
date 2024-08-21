package daehun.trip_java.Trip.controller;

import daehun.trip_java.Trip.domain.Trip;
import daehun.trip_java.Trip.service.TripService;
import daehun.trip_java.User.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    return "newTrip";
  }

  @PostMapping
  public String createTrip(@AuthenticationPrincipal User user, @ModelAttribute Trip trip) {
    tripService.createTrip(user.getUsername(), trip.getTripName(), trip.getStartDate(), trip.getEndDate());
    return "redirect:/trips";
  }
}