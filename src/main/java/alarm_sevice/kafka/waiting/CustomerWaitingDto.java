package alarm_sevice.kafka.waiting;

import alarm_sevice.domain.alarm.enums.ServiceType;

import java.time.LocalDateTime;

public record CustomerWaitingDto(
        Long userId,
        ServiceType serviceType,
        Long serviceId,
        CustomerWaitingType customerWaitingType,
        String userName,
        Long people,
        LocalDateTime date,
        Long waiting

) {
}
