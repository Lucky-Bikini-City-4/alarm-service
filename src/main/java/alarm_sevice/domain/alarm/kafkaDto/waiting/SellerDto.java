package alarm_sevice.domain.alarm.kafkaDto.waiting;

import alarm_sevice.domain.alarm.enums.ServiceType;

public record SellerDto(

        Long userId,
        ServiceType serviceType,
        Long serviceId,

        SellerAlarmType type,
        String customerName

) {
}
