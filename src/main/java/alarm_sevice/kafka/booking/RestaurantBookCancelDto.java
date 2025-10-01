package alarm_sevice.kafka.booking;

import alarm_sevice.domain.alarm.enums.ServiceType;

public record RestaurantBookCancelDto(
        Long userId,
        ServiceType serviceType,
        Long serviceId,
        String restaurantName,
        String userName,
        String reason
) {
}
