package alarm_sevice.domain.alarm.kafkaDto.waiting;

import alarm_sevice.domain.alarm.enums.ServiceType;
import org.springframework.cglib.core.Local;

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
