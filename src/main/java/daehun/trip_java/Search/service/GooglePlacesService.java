package daehun.trip_java.Search.service;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResult;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GooglePlacesService {

  private final GeoApiContext context;

  public GooglePlacesService(@Value("${google.maps.api.key}") String apiKey)  {
    this.context = new GeoApiContext.Builder()
        .apiKey(apiKey)
        .build();
  }

  // 기존 장소 검색
  public PlacesSearchResult searchPlace(String placeName) {
    try {
      return PlacesApi.textSearchQuery(context, placeName).await().results[0];
    } catch (Exception e) {
      throw new RuntimeException("장소 세부정보를 가져오지 못했습니다.", e);
    }
  }

  // 특정 위치 기준으로 반경 5km 내의 인기 장소 검색
  public List<PlacesSearchResult> searchNearbyPlaces(double latitude, double longitude, int radius) {
    try {
      return Arrays.asList(PlacesApi.nearbySearchQuery(context, new LatLng(latitude, longitude))
          .radius(radius)
          .await()
          .results);
    } catch (Exception e) {
      throw new RuntimeException("주변 장소를 가져오지 못했습니다.", e);
    }
  }
}