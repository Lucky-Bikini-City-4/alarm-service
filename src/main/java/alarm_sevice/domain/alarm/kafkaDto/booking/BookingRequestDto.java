package alarm_sevice.domain.alarm.kafkaDto.booking;

import alarm_sevice.domain.alarm.enums.ServiceType;

import java.time.LocalDateTime;

public record BookingRequestDto(

        Long userId,
        ServiceType serviceType,
        Long serviceId,
        String userName,
        String serviceName,
        LocalDateTime date


) {
}
