package alarm_sevice.domain.alarm.kafkaDto.booking;

import alarm_sevice.domain.alarm.enums.ServiceType;

import java.time.LocalDateTime;

public record RestaurantBookDto(
        Long userId,
        ServiceType serviceType,
        Long serviceId,
        String restaurantName,
        String userName,
        Integer people,
        LocalDateTime date

) {
}
