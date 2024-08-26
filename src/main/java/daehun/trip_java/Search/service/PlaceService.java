package daehun.trip_java.Search.service;

import com.google.maps.model.PlacesSearchResult;
import daehun.trip_java.Search.domain.Place;
import daehun.trip_java.Search.repository.PlaceRepository;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PlaceService {

  private final PlaceRepository placeRepository;
  private final GooglePlacesService googlePlacesService;

  public PlaceService(PlaceRepository placeRepository, GooglePlacesService googlePlacesService) {
    this.placeRepository = placeRepository;
    this.googlePlacesService = googlePlacesService;
  }

  // 기존 장소 검색
  public Place getPlaceByName(String name) {
    Optional<Place> existingPlace = placeRepository.findByName(name);

    // ES에 저장된 데이터가 있는 경우 :데이터 최신성 확인
    if (existingPlace.isPresent()) {
      Place place = existingPlace.get();

      // 최신성 검증 로직
      if (isDataStale(place)) {
        PlacesSearchResult googlePlace = googlePlacesService.searchPlace(name);
        Place updatedPlace = mapToPlace(googlePlace);
        updatedPlace.setId(place.getId()); // 기존 데이터 ID 유지
        placeRepository.save(updatedPlace);
        return updatedPlace;
      }

      return place; // 최신 데이터 반환
    }

    // ES에 데이터가 없을 경우 Google Places API 호출 후 저장
    PlacesSearchResult googlePlace = googlePlacesService.searchPlace(name);
    Place newPlace = mapToPlace(googlePlace);
    return placeRepository.save(newPlace);
  }

  // ID로 장소 검색
  public Optional<Place> getPlaceById(String id) {
    return placeRepository.findById(id);
  }

  // ES 데이터 최신성 확인 로직(1일 1회)
  private boolean isDataStale(Place place) {
    return place.getUpdatedAt().isBefore(LocalDateTime.now().minusDays(1));
  }

  // 특정 위치 기준으로 반경 5km 내 인기 장소 검색 및 ES에 저장
  public List<Place> getNearbyPlaces(double latitude, double longitude) {
    List<PlacesSearchResult> googlePlaces = googlePlacesService.searchNearbyPlaces(latitude, longitude, 5000);

    return googlePlaces.stream()
        .map(this::mapToPlace)
        .sorted(Comparator.comparingDouble(Place::getRating).reversed()) // 평점에 따라 정렬
        .map(placeRepository::save) // ES 저장
        .collect(Collectors.toList());
  }

  // GooglePlaces API 결과 -> Place 엔티티로 매핑
  private Place mapToPlace(PlacesSearchResult googlePlace) {
    Place place = new Place();
    place.setName(googlePlace.name);
    place.setAddress(googlePlace.formattedAddress);
    place.setLatitude(googlePlace.geometry.location.lat);
    place.setLongitude(googlePlace.geometry.location.lng);
    place.setRating(googlePlace.rating);
    place.setUpdatedAt(LocalDateTime.now()); // 최신성 확인
    return place;
  }
}