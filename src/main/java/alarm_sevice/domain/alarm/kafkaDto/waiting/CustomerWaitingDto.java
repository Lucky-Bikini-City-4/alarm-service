package alarm_sevice.domain.alarm.kafkaDto.waiting;

import alarm_sevice.domain.alarm.enums.ServiceType;

import java.time.LocalDateTime;

public record CustomerWaitingDto(
        Long userId,
        ServiceType serviceType,
        Long serviceId,
        CustomerWaitingType customerWaitingType,
        String userName,
        Integer people,
        LocalDateTime date,
        Integer waiting

) {
}
