package alarm_sevice.domain.alarm.dto;

import alarm_sevice.domain.alarm.enums.ServiceType;

public record CreateRequestDto(
    Long userId,
    ServiceType serviceType,
    Long serviceId,
    String title,
    String content
) {
}
