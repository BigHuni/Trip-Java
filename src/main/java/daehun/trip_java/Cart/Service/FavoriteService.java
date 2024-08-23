package daehun.trip_java.Cart.Service;

import daehun.trip_java.Cart.Repository.FavoriteRepository;
import daehun.trip_java.Cart.domain.Favorite;
import daehun.trip_java.Trip.domain.Trip;
import daehun.trip_java.Trip.repository.TripRepository;
import daehun.trip_java.User.domain.User;
import java.awt.print.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {
  private final FavoriteRepository favoriteRepository;
  private final TripRepository tripRepository;

  // 사용자가 여행을 즐겨찾기에 추가
  public Favorite addFavorite(User user, Long tripId) {
    Trip trip = tripRepository.findById(tripId)
        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 여행 ID입니다."));

    // 이미 즐겨찾기에 추가된 경우 예외 처리
    if (favoriteRepository.existsByUserAndTrip(user, trip)) {
      throw new IllegalArgumentException("이미 즐겨찾기에 추가된 여행입니다.");
    }

    Favorite favorite = new Favorite();
    favorite.setUser(user);
    favorite.setTrip(trip);
    return favoriteRepository.save(favorite);
  }

  // 특정 사용자의 모든 즐겨찾기 항목 조회
  public Page<Favorite> getFavoritesByUser(User user, Pageable pageable) {
    return favoriteRepository.findByUser(user, pageable);
  }

  // 사용자의 즐겨찾기에서 특정 여행 제거
  public void removeFavorite(User user, Long tripId) {
    Trip trip = tripRepository.findById(tripId)
        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 여행 ID입니다."));
    favoriteRepository.deleteByUserAndTrip(user, trip);
  }

  // 즐겨찾기 여부 확인
  public boolean isFavorite(User user, Long tripId) {
    Trip trip = tripRepository.findById(tripId)
        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 여행 ID입니다."));
    return favoriteRepository.existsByUserAndTrip(user, trip);
  }
}
