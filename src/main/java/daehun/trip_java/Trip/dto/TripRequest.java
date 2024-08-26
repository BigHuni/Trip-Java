package daehun.trip_java.Trip.dto;

import daehun.trip_java.Trip.domain.Trip;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripRequest {
  private Trip trip;
  private List<String> placeIds;
}
