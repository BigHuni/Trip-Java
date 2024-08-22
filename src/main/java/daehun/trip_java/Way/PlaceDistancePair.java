package daehun.trip_java.Way;

import daehun.trip_java.Search.domain.Place;

class PlaceDistancePair {

  private Place place;
  private double distance;

  public PlaceDistancePair(Place place, double distance) {
    this.place = place;
    this.distance = distance;
  }

  public Place getPlace() {
    return place;
  }

  public double getDistance() {
    return distance;
  }
}
