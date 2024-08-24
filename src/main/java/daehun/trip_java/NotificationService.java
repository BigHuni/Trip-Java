package daehun.trip_java;

import daehun.trip_java.Checklist.Repository.ChecklistRepository;
import daehun.trip_java.Checklist.domain.Checklist;
import daehun.trip_java.Trip.domain.Trip;
import daehun.trip_java.Trip.repository.TripRepository;
import daehun.trip_java.User.domain.User;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final TripRepository tripRepository;
  private final ChecklistRepository checklistRepository;
  private final JavaMailSender mailSender;

  @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
  public void sendTripNotifications() {
    LocalDate today = LocalDate.now();

    // 모든 여행 조회
    List<Trip> trips = tripRepository.findAll();
    for (Trip trip : trips) {
      User user = trip.getUser();
      LocalDate startDate = trip.getStartDate();
      LocalDate endDate = trip.getEndDate();

      // 여행 시작 및 종료 알림
      if (startDate.minusDays(1).isEqual(today) || endDate.plusDays(1).isEqual(today)) {
        sendEmail(user.getEmail(), "여행 알림", generateTripNotificationMessage(trip));
      }

      // 체크리스트 미완료 항목 알림
      List<Checklist> checklists = checklistRepository.findByTrip(trip);
      List<Checklist> incompleteChecklists = checklists.stream()
          .filter(checklist -> !checklist.getIsCompleted())
          .toList();

      if (!incompleteChecklists.isEmpty()) {
        sendEmail(user.getEmail(), "체크리스트 미완료 항목 알림", generateChecklistNotificationMessage(trip, incompleteChecklists));
      }
    }
  }

  private void sendEmail(String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    mailSender.send(message);
  }

  private String generateTripNotificationMessage(Trip trip) {
    return "안녕하세요, " + trip.getUser().getUsername() + "님.\n" +
        "여행 '" + trip.getTripName() + "'이(가) " +
        (trip.getStartDate().minusDays(1).isEqual(LocalDate.now()) ? "내일 시작" : "오늘 종료") +
        "됩니다.\n여행을 준비하세요!";
  }

  private String generateChecklistNotificationMessage(Trip trip, List<Checklist> incompleteChecklists) {
    StringBuilder message = new StringBuilder();
    message.append("안녕하세요, ").append(trip.getUser().getUsername()).append("님.\n")
        .append("여행 '").append(trip.getTripName()).append("'의 체크리스트에 미완료 항목이 있습니다.\n");

    incompleteChecklists.forEach(checklist ->
        message.append("- ").append(checklist.getItem()).append("\n")
    );

    return message.toString();
  }
}