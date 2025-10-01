package alarm_sevice.kafka.waiting;

import alarm_sevice.domain.alarm.enums.ServiceType;

public record CustomerFromSellerCancelDto(
        Long userId,
        ServiceType serviceType,
        Long serviceId
) {
}
