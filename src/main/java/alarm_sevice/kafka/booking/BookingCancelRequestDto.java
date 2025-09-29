package alarm_sevice.kafka.booking;

import alarm_sevice.domain.alarm.enums.ServiceType;

public record BookingCancelRequestDto(
        Long userId,
        ServiceType serviceType,
        Long serviceId,
        String serviceName

) {
}
