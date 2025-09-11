package alarm_sevice.domain.alarm.dto;

import alarm_sevice.domain.alarm.Alarm;
import alarm_sevice.domain.alarm.enums.AlarmType;
import alarm_sevice.domain.alarm.enums.ServiceType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResponseDto(
        Long alarmId,
        Long userId,
        AlarmType type,
        ServiceType serviceType,
        Long serviceId,
        String title,
        String content,
        LocalDateTime createdAt
) {

    public static ResponseDto from(Alarm alarm) {
        return ResponseDto.builder()
                .alarmId(alarm.getAlarmId())
                .userId(alarm.getUserId())
                .type(alarm.getType())
                .serviceType(alarm.getServiceType())
                .serviceId(alarm.getServiceId())
                .title(alarm.getTitle())
                .content(alarm.getContent())
                .createdAt(alarm.getCreatedAt())
                .build();
    }
}
