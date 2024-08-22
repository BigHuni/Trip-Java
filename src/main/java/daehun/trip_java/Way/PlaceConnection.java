package daehun.trip_java.Way;

import daehun.trip_java.Search.domain.Place;

// 특정 여행지(Place)와 여행지까지의 거리 저장
class PlaceConnection {

  private Place destination;
  private double distance;

  public PlaceConnection(Place destination, double distance) {
    this.destination = destination;
    this.distance = distance;
  }

  // 목적지 여행지(Place) 반환
  public Place getDestination() {
    return destination;
  }

  // 출발지 - 목적지 사이 간 거리 반환
  public double getDistance() {
    return distance;
  }
}
