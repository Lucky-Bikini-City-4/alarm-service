package alarm_sevice.domain.alarm;

import alarm_sevice.domain.alarm.enums.AlarmType;
import alarm_sevice.domain.alarm.enums.ServiceType;
import alarm_sevice.utils.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Data
@Entity(name = "alarms")
@NoArgsConstructor
@Table
public class Alarm extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlarmType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Column(nullable = false)
    private Long serviceId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    public Alarm(Long userId, AlarmType type, ServiceType serviceType, Long serviceId, String title, String content) {
        this.userId = userId;
        this.type = type;
        this.serviceType = serviceType;
        this.serviceId = serviceId;
        this.title = title;
        this.content = content;
    }
}
