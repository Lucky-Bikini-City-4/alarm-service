package alarm_sevice.domain.alarm.kafkaDto.register;

import alarm_sevice.domain.alarm.enums.ServiceType;

public record ServiceRegisterRequestDto(
        Long userId,
        ServiceType serviceType,
        Long serviceId,
        String userName,
        Status status

) {
}
