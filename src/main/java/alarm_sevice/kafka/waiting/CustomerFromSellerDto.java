package alarm_sevice.kafka.waiting;

import alarm_sevice.domain.alarm.enums.ServiceType;

import java.time.LocalDateTime;

public record CustomerFromSellerDto(

        Long userId,
        ServiceType serviceType,
        Long serviceId,
        FromSellerType type,
        String userName,
        LocalDateTime deadline

) {
}
