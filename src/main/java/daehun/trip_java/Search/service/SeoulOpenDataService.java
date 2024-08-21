package daehun.trip_java.Search.service;

import daehun.trip_java.Search.domain.Place;
import daehun.trip_java.Search.dto.TouristAttractionsResponse;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SeoulOpenDataService {

  private static final Logger logger = LoggerFactory.getLogger(SeoulOpenDataService.class);

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

    try{
      TouristAttractionsResponse response = restTemplate.getForObject(url, TouristAttractionsResponse.class);
      return mapToPlaces(response);
    } catch (Exception e) {
      logger.error("서울시 오픈데이터 API에서 관광명소 정보를 가져오는데 실패했습니다.", e);
      throw new RuntimeException("서울시 오픈데이터 API에서 관광명소 정보를 가져오는데 실패했습니다.", e);
    }
  }

  private List<Place> mapToPlaces(TouristAttractionsResponse response) {
    List<Place> places = new ArrayList<>();

    try {
      if (response != null && response.getTouristAttractionsInfo() != null) {
        for (TouristAttractionsResponse.TouristAttraction attraction : response.getTouristAttractionsInfo()
            .getAttractions()) {
          Place place = new Place();
          place.setName(attraction.getName());
          place.setAddress(attraction.getAddress());
          place.setLatitude(attraction.getLatitude());
          place.setLongitude(attraction.getLongitude());
          places.add(place);
        }
      }
    } catch (Exception e) {
      logger.error("관광명소를 호출하는 과정에서 오류가 발생했습니다.", e);
      throw new RuntimeException("관광명소를 호출하는 과정에서 오류가 발생했습니다.", e);
    }
    return places;
  }
}