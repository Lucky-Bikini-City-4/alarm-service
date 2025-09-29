package alarm_sevice.kafka.booking;

import alarm_sevice.domain.alarm.enums.ServiceType;

import java.time.LocalDateTime;

public record RestaurantBookConfirmDto(
        Long userId,
        ServiceType serviceType,
        Long serviceId,
        String restaurantName,
        String userName,
        Integer people,
        LocalDateTime date
) {
}
