package daehun.trip_java.Search.service;

import daehun.trip_java.Search.domain.Place;
import daehun.trip_java.Search.dto.TouristAttractionsResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SeoulOpenDataService {

  private final RestTemplate restTemplate;

  @Value("${seoul.api.key}")
  private String apiKey;

  public SeoulOpenDataService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public List<Place> getTouristAttractions() {
    String url = UriComponentsBuilder.fromHttpUrl("http://openapi.seoul.go.kr:8088")
        .pathSegment(apiKey, "json", "TouristAttractionsInfo", "1", "1000")
        .build()
        .toUriString();

    TouristAttractionsResponse response = restTemplate.getForObject(url, TouristAttractionsResponse.class);

    return mapToPlaces(response);
  }

  private List<Place> mapToPlaces(TouristAttractionsResponse response) {
    List<Place> places = new ArrayList<>();
    if (response != null && response.getTouristAttractionsInfo() != null) {
      for (TouristAttractionsResponse.TouristAttraction attraction : response.getTouristAttractionsInfo().getAttractions()) {
        Place place = new Place();
        place.setName(attraction.getName());
        place.setAddress(attraction.getAddress());
        place.setLatitude(attraction.getLatitude());
        place.setLongitude(attraction.getLongitude());
        places.add(place);
      }
    }
    return places;
  }
}