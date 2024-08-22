package daehun.trip_java.Way;

import daehun.trip_java.Search.domain.Place;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

// 다익스트라 알고리즘 -> 최단 경로 계산
public class DijkstraAlgorithm {

  public Map<Place, Double> calculateShortestPath(PlaceGraph graph, Place source) {
    Map<Place, Double> distances = new HashMap<>();
    Map<Place, Place> previousPlaces = new HashMap<>();
    PriorityQueue<PlaceDistancePair> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(PlaceDistancePair::getDistance));

    for (Place place : graph.getPlaces()) {
      if (place.equals(source)) {
        distances.put(place, 0.0);
      } else {
        distances.put(place, Double.MAX_VALUE);
      }
      priorityQueue.add(new PlaceDistancePair(place, distances.get(place)));
    }

    while (!priorityQueue.isEmpty()) {
      Place currentPlace = priorityQueue.poll().getPlace();

      for (PlaceConnection connection : graph.getConnections(currentPlace)) {
        Place destination = connection.getDestination();
        double newDist = distances.get(currentPlace) + connection.getDistance();

        if (newDist < distances.get(destination)) {
          distances.put(destination, newDist);
          previousPlaces.put(destination, currentPlace);

          priorityQueue.add(new PlaceDistancePair(destination, newDist));
        }
      }
    }

    return distances;
  }
}

