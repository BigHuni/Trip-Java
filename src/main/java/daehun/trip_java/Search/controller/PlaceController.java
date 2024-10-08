package daehun.trip_java.Search.controller;

import daehun.trip_java.Search.domain.Place;
import daehun.trip_java.Search.service.PlaceDataService;
import daehun.trip_java.Search.service.PlaceService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/places")
public class PlaceController {

  private final PlaceService placeService;
  private final PlaceDataService placeDataService;

  public PlaceController(PlaceService placeService, PlaceDataService placeDataService) {
    this.placeService = placeService;
    this.placeDataService = placeDataService;
  }

  // 기존 장소 이름으로 검색
  @GetMapping("/name/{name}")
  public ResponseEntity<Place> getPlace(@PathVariable String name) {
    Place place = placeService.getPlaceByName(name);
    return ResponseEntity.ok(place);
  }

  // 장소 ID로 검색
  @GetMapping("/id/{id}")
  public ResponseEntity<Place> getPlaceById(@PathVariable String id) {
    Optional<Place> place = placeService.getPlaceById(id);
    return place.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  // 반경 내 장소 검색
  @GetMapping("/nearby")
  public ResponseEntity<List<Place>> getNearbyPlaces(@RequestParam double latitude, @RequestParam double longitude) {
    List<Place> places = placeService.getNearbyPlaces(latitude, longitude);
    return ResponseEntity.ok(places);
  }

  // 서울시 관광지 데이터 수집
  @PostMapping("/api/collect-seoul-data")
  public ResponseEntity<String> collectSeoulData() {
    placeDataService.saveTouristAttractions();
    return ResponseEntity.ok("서울시 관광지 데이터가 수집되어 Elasticsearch에 저장되었습니다.");
  }
}