package alarm_sevice.domain.alarm.kafkaDto.backoffice;

import alarm_sevice.domain.alarm.enums.ServiceType;

public record BackofficeRegisterDto(
        Long userId,
        ServiceType serviceType,
        Long serviceId,
        ServiceType requestServiceType,
        String requestServiceName,
        String requestUserName
) {
}
