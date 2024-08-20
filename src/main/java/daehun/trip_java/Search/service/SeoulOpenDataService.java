package daehun.trip_java.Search.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import daehun.trip_java.Search.domain.Place;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SeoulOpenDataService {

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  @Value("${seoul.api.key}")
  private String apiKey;

  public SeoulOpenDataService(RestTemplate restTemplate, ObjectMapper objectMapper) {
    this.restTemplate = restTemplate;
    this.objectMapper = objectMapper;
  }

  public List<Place> getTouristAttractions() {
    String url = String.format("http://openapi.seoul.go.kr:8088/%s/json/TouristAttractionsInfo/1/1000/", apiKey);
    String response = restTemplate.getForObject(url, String.class);

    return parseResponse(response);
  }

  private List<Place> parseResponse(String response) {
    List<Place> places = new ArrayList<>();
    try {
      JsonNode root = objectMapper.readTree(response);
      JsonNode items = root.path("TouristAttractionsInfo").path("row");

      for (JsonNode item : items) {
        Place place = new Place();
        place.setName(item.path("POST_SJ").asText());
        place.setAddress(item.path("ADDRESS").asText());
        place.setLatitude(item.path("Y").asDouble());
        place.setLongitude(item.path("X").asDouble());
        places.add(place);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return places;
  }
}