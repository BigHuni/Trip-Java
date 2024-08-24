package daehun.trip_java.Checklist.Service;

import daehun.trip_java.Checklist.Repository.ChecklistRepository;
import daehun.trip_java.Checklist.domain.Checklist;
import daehun.trip_java.Trip.domain.Trip;
import daehun.trip_java.Trip.repository.TripRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChecklistService {

  private final ChecklistRepository checklistRepository;
  private final TripRepository tripRepository;

  // 체크리스트 항목 생성
  public Checklist createChecklist(Long tripId, String item, String memo) {
    Trip trip = tripRepository.findById(tripId)
        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 여행 ID입니다."));

    Checklist checklist = new Checklist();
    checklist.setTrip(trip);
    checklist.setItem(item);
    checklist.setMemo(memo);
    checklist.setIsCompleted(false); // 초기값은 완료되지 않음
    return checklistRepository.save(checklist);
  }

  // 특정 여행에 대한 체크리스트 조회
  public List<Checklist> getChecklistsByTrip(Long tripId) {
    Trip trip = tripRepository.findById(tripId)
        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 여행 ID입니다."));
    return checklistRepository.findByTrip(trip);
  }

  // 체크리스트 항목 수정
  public Checklist updateChecklist(Long checkId, String item, String memo, boolean isCompleted) {
    Checklist checklist = checklistRepository.findById(checkId)
        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 체크리스트 ID입니다."));
    checklist.setItem(item);
    checklist.setMemo(memo);
    checklist.setIsCompleted(isCompleted);
    return checklistRepository.save(checklist);
  }

  // 체크리스트 항목 삭제
  public void deleteChecklist(Long checkId) {
    checklistRepository.deleteById(checkId);
  }

  // 준비 완료 진행률 계산
  public double calculateCompletionRate(Long tripId) {
    Trip trip = tripRepository.findById(tripId)
        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 여행 ID입니다."));
    List<Checklist> checklists = checklistRepository.findByTrip(trip);

    if (checklists.isEmpty()) {
      return 0.0;
    }

    long completedCount = checklists.stream()
        .filter(Checklist::isCompleted)
        .count();

    return (double) completedCount / checklists.size() * 100;
  }
}