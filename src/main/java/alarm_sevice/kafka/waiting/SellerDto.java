package alarm_sevice.kafka.waiting;

import alarm_sevice.domain.alarm.enums.ServiceType;

public record SellerDto(

        Long userId,
        ServiceType serviceType,
        Long serviceId,

        SellerAlarmType type,
        String customerName

) {
}
