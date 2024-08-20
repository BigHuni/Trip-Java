package daehun.trip_java.Search.controller;

import daehun.trip_java.Search.domain.Place;
import daehun.trip_java.Search.service.PlaceDataService;
import daehun.trip_java.Search.service.PlaceService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  @GetMapping("/{name}")
  public Place getPlace(@PathVariable String name) {
    return placeService.getPlaceByName(name);
  }

  // 반경 내 장소 검색
  @GetMapping("/nearby")
  public List<Place> getNearbyPlaces(@RequestParam double latitude, @RequestParam double longitude) {
    return placeService.getNearbyPlaces(latitude, longitude);
  }

  @GetMapping("/api/collect-seoul-data")
  public String collectSeoulData() {
    placeDataService.saveTouristAttractions();
    return "서울시 관광지 데이터가 수집되어 Elasticsearch에 저장되었습니다.";
  }
}