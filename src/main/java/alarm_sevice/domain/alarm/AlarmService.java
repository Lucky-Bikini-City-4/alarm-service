package alarm_sevice.domain.alarm;

import alarm_sevice.domain.alarm.dto.CreateRequestDto;
import alarm_sevice.domain.alarm.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final KafkaTemplate<String, RestaurantBookDto> kafkaTemplate1;
    private final KafkaTemplate<String, RestaurantBookConfirmDto> kafkaTemplate2;
    private final static Long TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;


    public void sendMessageQueue1(String topic, String key, RestaurantBookDto dto) {
        kafkaTemplate1.send(topic, key, dto);
    }

    public void sendMessageQueue2(String topic, String key, RestaurantBookConfirmDto dto) {
        kafkaTemplate2.send(topic, key, dto);
    }

    @KafkaListener(groupId = "booking", topics = "restaurant-book")
    @Transactional
    public void restaurantBook(RestaurantBookDto dto) {
        String title = dto.restaurantName() + " 예약 요청이 완료되었습니다";
        String content = "예약자 : " + dto.userName() + "\n예약 인원 : " + dto.people() + "\n일시 : " + dto.date();
        Alarm alarm = new Alarm(dto.userId(), dto.serviceType(), dto.serviceId(), title, content);
        createAlarm(alarm);
        sendAlarmSSE(alarm);
    }

    @KafkaListener(groupId = "booking", topics = "restaurant-book-confirm")
    @Transactional
    public void restaurantBookConfirm(RestaurantBookConfirmDto dto) {
        String title = dto.restaurantName() + " 예약이 확정되었습니다";
        String content = "예약자 : " + dto.userName() + "\n예약 인원 : " + dto.people() + "\n일시 : " + dto.date();
        Alarm alarm = new Alarm(dto.userId(), dto.serviceType(), dto.serviceId(), title, content);
        createAlarm(alarm);
        sendAlarmSSE(alarm);
    }

    @KafkaListener(groupId = "booking", topics = "restaurant-book-cancel")
    @Transactional
    public void restaurantBookCancel(RestaurantBookCancelDto dto) {
        String title = dto.restaurantName() + " 예약이 취소되었습니다";
        String content = "예약자 : " + dto.userName() + "\n사유 : " + dto.reason();
        Alarm alarm = new Alarm(dto.userId(), dto.serviceType(), dto.serviceId(), title, content);
        createAlarm(alarm);
        sendAlarmSSE(alarm);
    }

    @KafkaListener(groupId = "booking", topics = "booking")
    @Transactional
    public void booking(BookingRequestDto dto) {
        String title = dto.serviceName() + " 예매가 완료되었습니다.";
        String content = "예약자 : " + dto.userName() + "\n일시 : " + dto.date();
        Alarm alarm = new Alarm(dto.userId(), dto.serviceType(), dto.serviceId(), title, content);
        createAlarm(alarm);
        sendAlarmSSE(alarm);
    }

    @KafkaListener(groupId = "booking", topics = "booking-cancel")
    @Transactional
    public void bookingCancel(BookingCancelRequestDto dto) {
        String title = dto.serviceName() + " 예매가 취소되었습니다.";
        String content = "자세한 내용은 상세 페이지를 확인해주세요.";
        Alarm alarm = new Alarm(dto.userId(), dto.serviceType(), dto.serviceId(), title, content);
        createAlarm(alarm);
        sendAlarmSSE(alarm);
    }

    @KafkaListener(groupId = "register", topics = "restaurant")
    @Transactional
    public void registerRestaurant(ServiceRegisterRequestDto dto) {
        String title = null;
        switch (dto.status()) {
            case REQUESTED -> title = "음식점 등록 요청이 완료되었습니다.";
            case APPROVED -> title = "음식점 등록이 승인되었습니다.";
            case DECLINED -> title = "음식점 등록이 거부되었습니다.";
        }
        String content = "";
        Alarm alarm = new Alarm(dto.userId(), dto.serviceType(), dto.serviceId(), title, content);
        createAlarm(alarm);
        sendAlarmSSE(alarm);
    }

    @KafkaListener(groupId = "register", topics = "exhibition")
    @Transactional
    public void registerExhibition(ServiceRegisterRequestDto dto) {
        String title = null;
        switch (dto.status()) {
            case REQUESTED -> title = "전시회 등록 요청이 완료되었습니다.";
            case APPROVED -> title = "전시회 등록이 승인되었습니다.";
            case DECLINED -> title = "전시회 등록이 거부되었습니다.";
        }
        String content = "";
        Alarm alarm = new Alarm(dto.userId(), dto.serviceType(), dto.serviceId(), title, content);
        createAlarm(alarm);
        sendAlarmSSE(alarm);
    }

    @KafkaListener(groupId = "register", topics = "Performance")
    @Transactional
    public void registerPerformance(ServiceRegisterRequestDto dto) {
        String title = null;
        switch (dto.status()) {
            case REQUESTED -> title = "공연 등록 요청이 완료되었습니다.";
            case APPROVED -> title = "공연 등록이 승인되었습니다.";
            case DECLINED -> title = "공연 등록이 거부되었습니다.";
        }
        String content = "";
        Alarm alarm = new Alarm(dto.userId(), dto.serviceType(), dto.serviceId(), title, content);
        createAlarm(alarm);
        sendAlarmSSE(alarm);
    }

    @KafkaListener(groupId = "backoffice", topics = "register")
    @Transactional
    public void backofficeRegister(BackofficeRegisterDto dto) {
        String title = "새로운 서비스 등록 요청이 도착했습니다.";
        String content = "서비스 타입 : " + dto.requestServiceType() + "\n서비스 이름 : " + dto.requestServiceName() + "\n사용자 : " + dto.requestUserName();
        Alarm alarm = new Alarm(dto.userId(), dto.serviceType(), dto.serviceId(), title, content);
        createAlarm(alarm);
        sendAlarmSSE(alarm);
    }

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        emitterRepository.save(userId, emitter);

        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onCompletion(() -> emitterRepository.deleteById(userId));
        emitter.onTimeout(() -> emitterRepository.deleteById(userId));
        emitter.onError((e) -> emitterRepository.deleteById(userId));

        return emitter;
    }

    @Transactional
    public List<ResponseDto> getAllByUserId(Long userId) {
        List<Alarm> alarmList = alarmRepository.findAllByUserId(userId);
        return alarmList.stream()
                .map(ResponseDto::from)
                .toList();
    }

    @Transactional
    public ResponseDto getByAlarmId(Long alarmId) {
        Alarm alarm = alarmRepository.findByAlarmId(alarmId);
        return ResponseDto.from(alarm);
    }

    @Transactional
    public List<ResponseDto> getAll() {
        List<Alarm> alarmList = alarmRepository.findAll();
        return alarmList.stream()
                .map(ResponseDto::from)
                .toList();
    }

    @Transactional
    public ResponseDto create(CreateRequestDto requestDto) {
        Alarm alarm = new Alarm(
                requestDto.userId(),
                requestDto.type(),
                requestDto.serviceType(),
                requestDto.serviceId(),
                requestDto.title(),
                requestDto.content());
        Alarm savedAlarm = alarmRepository.save(alarm);
        return ResponseDto.from(savedAlarm);
    }
}
