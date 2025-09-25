package alarm_sevice.auth;

public record Passport(
        Long userId,

        UserRole role
) {
}
