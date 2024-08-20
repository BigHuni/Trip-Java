package daehun.trip_java.Search.service;

import daehun.trip_java.Search.domain.Place;
import daehun.trip_java.Search.repository.PlaceRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PlaceDataService {

  private final PlaceRepository placeRepository;
  private final SeoulOpenDataService seoulOpenDataService;

  public PlaceDataService(PlaceRepository placeRepository, SeoulOpenDataService seoulOpenDataService) {
    this.placeRepository = placeRepository;
    this.seoulOpenDataService = seoulOpenDataService;
  }

  public void saveTouristAttractions() {
    List<Place> places = seoulOpenDataService.getTouristAttractions();
    placeRepository.saveAll(places);
  }
}