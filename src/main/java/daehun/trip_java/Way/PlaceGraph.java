package daehun.trip_java.Way;

import daehun.trip_java.Search.domain.Place;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Place(장소) 간의 연결을 그래프로 표현한 클래스
public class PlaceGraph {
  private Map<Place, List<PlaceConnection>> adjacencyList;

  public PlaceGraph() {
    this.adjacencyList = new HashMap<>();  // 인접 리스트 방식 -> 그래프 표현, place = 그래프의 노드(여행지)
  }

  // 새로운 여행지(Place)를 그래프에 추가
  public void addPlace(Place place) {
    adjacencyList.putIfAbsent(place, new ArrayList<>());
  }

  // 두 여행지 간의 연결 추가
  public void addConnection(Place from, Place to) {
    double distance = DistanceCalculator.calculateDistance(
        from.getLatitude(), from.getLongitude(),
        to.getLatitude(), to.getLongitude());
    adjacencyList.get(from).add(new PlaceConnection(to, distance));
  }

  // 특정 여행지(Place)에 연결된 모든 다른 여행지들과의 연결 정보 반환
  public List<PlaceConnection> getConnections(Place place) {
    return adjacencyList.get(place);
  }

  // 그래프에 추가된 모든 여행지(Place)를 반환
  public Set<Place> getPlaces() {
    return adjacencyList.keySet();
  }
}

