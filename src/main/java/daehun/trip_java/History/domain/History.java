package daehun.trip_java.History.domain;

import daehun.trip_java.Trip.domain.Trip;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class History {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long historyId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tripId", nullable = false)
  private Trip trip;

  @Column(nullable = false)
  private Long placeId;

  @Column(nullable = false)
  private Integer sequence;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }
}