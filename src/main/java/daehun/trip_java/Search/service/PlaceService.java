package daehun.trip_java.Search.service;

import com.google.maps.model.PlacesSearchResult;
import daehun.trip_java.Search.domain.Place;
import daehun.trip_java.Search.repository.PlaceRepository;
import java.util.Comparator;
import java.util.List;
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
    return placeRepository.findByName(name)
        .orElseGet(() -> {
          PlacesSearchResult googlePlace = googlePlacesService.searchPlace(name);
          Place place = mapToPlace(googlePlace);
          return placeRepository.save(place);
        });
  }

  // 특정 위치 기준으로 반경 5km 내 인기 장소 검색 및 ES에 저장
  public List<Place> getNearbyPlaces(double latitude, double longitude) {
    List<PlacesSearchResult> googlePlaces = googlePlacesService.searchNearbyPlaces(latitude, longitude, 5000);

    List<Place> places = googlePlaces.stream()
        .map(this::mapToPlace)
        .sorted(Comparator.comparingDouble(Place::getRating).reversed()) // 평점에 따라 정렬
        .map(placeRepository::save)
        .collect(Collectors.toList());

    return places;
  }

  // GooglePlaces API 결과 -> Place 엔티티로 매핑
  private Place mapToPlace(PlacesSearchResult googlePlace) {
    Place place = new Place();
    place.setName(googlePlace.name);
    place.setAddress(googlePlace.formattedAddress);
    place.setLatitude(googlePlace.geometry.location.lat);
    place.setLongitude(googlePlace.geometry.location.lng);
    place.setRating(googlePlace.rating);
    return place;
  }
}