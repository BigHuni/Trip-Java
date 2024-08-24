package daehun.trip_java.Checklist.domain;

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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "checklist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Checklist {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long checkId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "trip_id", nullable = false)
  private Trip trip;

  @Column(name = "item", nullable = false)
  private String item;

  @Column(name = "isCompleted", nullable = false)
  private Boolean isCompleted = false;

  @Column(name = "memo", length = 100)
  private String memo;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  public Boolean isCompleted() {
    return isCompleted;
  }
}