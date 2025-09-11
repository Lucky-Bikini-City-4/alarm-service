package alarm_sevice.domain.alarm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findAllByUserId(Long userId);
    Alarm findByAlarmId(Long alarmId);
}
