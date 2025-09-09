package alarm_sevice.domain.alarm.dto;

import alarm_sevice.domain.alarm.enums.AlarmType;
import alarm_sevice.domain.alarm.enums.ServiceType;

public record CreateRequestDto(
    Long userId,
    AlarmType type,
    ServiceType serviceType,
    Long serviceId,
    String title,
    String content
) {
}
