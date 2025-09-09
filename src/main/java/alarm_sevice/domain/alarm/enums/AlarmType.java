package alarm_sevice.domain.alarm.enums;

public enum AlarmType {
    BOOKING_REQUEST,  //예약요청
    BOOKING_CONFIRMED,  //예약확정
    BOOKING_CANCELED,  //예약취소
    REGISTER_REQUEST,  //등록요청
    REGISTER_CONFIRMED,  //등록허가
    WAITING_CONFIRMED,  //웨이팅등록
    WAITING_NOTIFICATION    //웨이팅알람
}
