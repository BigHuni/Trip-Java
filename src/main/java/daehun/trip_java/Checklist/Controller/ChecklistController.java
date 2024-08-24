package daehun.trip_java.Checklist.Controller;

import daehun.trip_java.Checklist.Service.ChecklistService;
import daehun.trip_java.Checklist.domain.Checklist;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checklists")
@RequiredArgsConstructor
public class ChecklistController {

  private final ChecklistService checklistService;

  // 체크리스트 항목 생성
  @PostMapping
  public ResponseEntity<Checklist> createChecklist(@RequestParam Long tripId,
      @RequestParam String item,
      @RequestParam String memo) {
    Checklist checklist = checklistService.createChecklist(tripId, item, memo);
    return ResponseEntity.ok(checklist);
  }

  // 특정 여행의 체크리스트 조회
  @GetMapping("/trip/{tripId}")
  public ResponseEntity<List<Checklist>> getChecklistsByTrip(@PathVariable Long tripId) {
    List<Checklist> checklists = checklistService.getChecklistsByTrip(tripId);
    return ResponseEntity.ok(checklists);
  }

  // 체크리스트 항목 수정
  @PutMapping("/{checkId}")
  public ResponseEntity<Checklist> updateChecklist(@PathVariable Long checkId,
      @RequestParam String item,
      @RequestParam String memo,
      @RequestParam boolean isCompleted) {
    Checklist updatedChecklist = checklistService.updateChecklist(checkId, item, memo, isCompleted);
    return ResponseEntity.ok(updatedChecklist);
  }

  // 체크리스트 항목 삭제
  @DeleteMapping("/{checkId}")
  public ResponseEntity<Void> deleteChecklist(@PathVariable Long checkId) {
    checklistService.deleteChecklist(checkId);
    return ResponseEntity.noContent().build();
  }

  // 준비 완료 진행률 계산
  @GetMapping("/trip/{tripId}/completion-rate")
  public ResponseEntity<Double> getCompletionRate(@PathVariable Long tripId) {
    double completionRate = checklistService.calculateCompletionRate(tripId);
    return ResponseEntity.ok(completionRate);
  }
}