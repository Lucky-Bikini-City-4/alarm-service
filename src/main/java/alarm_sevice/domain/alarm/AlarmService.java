package alarm_sevice.domain.alarm;

import alarm_sevice.domain.alarm.dto.ResponseDto;
import alarm_sevice.domain.alarm.kafkaDto.backoffice.BackofficeRegisterDto;
import alarm_sevice.domain.alarm.kafkaDto.booking.*;
import alarm_sevice.domain.alarm.kafkaDto.register.ServiceRegisterRequestDto;
import alarm_sevice.domain.alarm.kafkaDto.waiting.CustomerFromSellerCancelDto;
import alarm_sevice.domain.alarm.kafkaDto.waiting.CustomerFromSellerDto;
import alarm_sevice.domain.alarm.kafkaDto.waiting.CustomerWaitingDto;
import alarm_sevice.domain.alarm.kafkaDto.waiting.SellerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
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
        try {
            String title = dto.restaurantName() + " 예약 요청이 완료되었습니다";
            String content = "\n예약자 : " + dto.userName() + "\n예약 인원 : " + dto.people() + "\n일시 : " + dto.date();
            Alarm alarm = new Alarm(dto.userId(), dto.serviceType(), dto.serviceId(), title, content);
            createAlarm(alarm);
            sendAlarmSSE(alarm);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @KafkaListener(groupId = "booking", topics = "restaurant-book-confirm")
    @Transactional
    public void restaurantBookConfirm(RestaurantBookConfirmDto dto) {
        String title = dto.restaurantName() + " 예약이 확정되었습니다";
        String content = "\n예약자 : " + dto.userName() + "\n예약 인원 : " + dto.people() + "\n일시 : " + dto.date();
        Alarm alarm = new Alarm(dto.userId(), dto.serviceType(), dto.serviceId(), title, content);
        createAlarm(alarm);
        sendAlarmSSE(alarm);
    }

    @KafkaListener(groupId = "booking", topics = "restaurant-book-cancel")
    @Transactional
    public void restaurantBookCancel(RestaurantBookCancelDto dto) {
        String title = dto.restaurantName() + " 예약이 취소되었습니다";
        String content = "\n예약자 : " + dto.userName() + "\n사유 : " + dto.reason();
        Alarm alarm = new Alarm(dto.userId(), dto.serviceType(), dto.serviceId(), title, content);
        createAlarm(alarm);
        sendAlarmSSE(alarm);
    }

    @KafkaListener(groupId = "booking", topics = "booking")
    @Transactional
    public void booking(BookingRequestDto dto) {
        String title = dto.serviceName() + " 예매가 완료되었습니다.";
        String content = "\n예약자 : " + dto.userName() + "\n일시 : " + dto.date();
        Alarm alarm = new Alarm(dto.userId(), dto.serviceType(), dto.serviceId(), title, content);
        createAlarm(alarm);
        sendAlarmSSE(alarm);
    }

    @KafkaListener(groupId = "booking", topics = "booking-cancel")
    @Transactional
    public void bookingCancel(BookingCancelRequestDto dto) {
        String title = dto.serviceName() + " 예매가 취소되었습니다.";
        String content = "\n자세한 내용은 상세 페이지를 확인해주세요.";
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

    @KafkaListener(groupId = "register", topics = "performance")
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
        String content = "\n서비스 타입 : " + dto.requestServiceType() + "\n서비스 이름 : " + dto.requestServiceName() + "\n사용자 : " + dto.requestUserName();
        Alarm alarm = new Alarm(dto.userId(), dto.serviceType(), dto.serviceId(), title, content);
        createAlarm(alarm);
        sendAlarmSSE(alarm);
    }

    @KafkaListener(groupId = "waiting", topics = "waiting-seller")
    @Transactional
    public void waitingSeller(SellerDto dto) {
        String title = null;
        String content = null;
        switch (dto.type()) {
            case CUSTOMER_REPLIED -> {
                title = "손님이 호출에 대한 응답을 보냈습니다.\n";
                content = dto.customerName() + "님이 호출에 대한 응답을 보냈습니다.";
            }
            case CUSTOMER_ARRIVED -> {
                title = "손님이 도착했습니다.\n";
                content = dto.customerName() + "님이 호출에 대한 응답을 보냈습니다.";
            }
            case NEW_WAITING -> {
                title = "새로운 웨이팅이 등록되었습니다.\n";
                content = dto.customerName() + "님이 웨이팅을 등록하였습니다.";
            }
            case WAITING_CANCELED -> {
                title = "웨이팅이 취소되었습니다.\n";
                content = dto.customerName() + "님이 웨이팅을 취소하셨습니다.";
            }
        }
        Alarm alarm = new Alarm(dto.userId(), dto.serviceType(), dto.serviceId(), title, content);
        createAlarm(alarm);
        sendAlarmSSE(alarm);
    }

    @KafkaListener(groupId = "waiting", topics = "waiting-number")
    @Transactional
    public void waitingNumber(CustomerWaitingDto dto) {
        String title = null;
        switch (dto.customerWaitingType()) {
            case PUT -> title = "웨이팅 등록이 완료되었습니다.\n";
            case SOON -> title = "웨이팅 순번이 임박했습니다.\n";
        }
        String content = "예약자 : " + dto.userName() + "\n예약인원 : " + dto.people() +
                "\n내 앞 대기 팀 수 : " + dto.waiting() + "\n일시 : " + dto.date();
        Alarm alarm = new Alarm(dto.userId(), dto.serviceType(), dto.serviceId(), title, content);
        createAlarm(alarm);
        sendAlarmSSE(alarm);
    }

    @KafkaListener(groupId = "waiting", topics = "waiting-call")
    @Transactional
    public void waitingNumber(CustomerFromSellerDto dto) {
        String title = "웨이팅 호출 안내문\n";
        String content = null;
        switch (dto.type()) {
            case FIRST -> content = dto.userName() + "님이 호출되셨습니다. 사장님께 도착 혹은 가고 있다고 응답해주세요 !";
            case LAST -> content = dto.userName() + "님이 호출되셨습니다. 응답을 하지 않을 경우 웨이팅이 취소됩니다.";
        }
        content += "\n응답 기한 : " + dto.deadline();
        Alarm alarm = new Alarm(dto.userId(), dto.serviceType(), dto.serviceId(), title, content);
        createAlarm(alarm);
        sendAlarmSSE(alarm);
    }

    @KafkaListener(groupId = "waiting", topics = "waiting-cancel")
    @Transactional
    public void waitingNumber(CustomerFromSellerCancelDto dto) {
        String title = "웨이팅이 취소되었습니다.\n";
        String content = "사장님의 호출에 응답하지 않아 웨이팅이 취소되었습니다.";
        Alarm alarm = new Alarm(dto.userId(), dto.serviceType(), dto.serviceId(), title, content);
        createAlarm(alarm);
        sendAlarmSSE(alarm);
    }

    public SseEmitter subscribe(Long userId, String lastEventId) {

        String id = createSseIdWithCurrentTime(userId);
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        emitterRepository.save(id, emitter);

        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));
        emitter.onError((e) -> emitterRepository.deleteById(id));

        //503 에러 방지
        sendToClient(emitter, id, "Successfully Connected. [userId = " + userId + " ]");

        if (!lastEventId.isEmpty()) {
            Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(userId));
            log.info("*** [ {} Events Occurred While Unconnected ] ***", eventCaches.size());
            eventCaches.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> {
                        sendToClient(emitter, entry.getKey(), entry.getValue());
                    });
        }
        return emitter;
    }

    @Transactional
    public Map<String, Object> allCache() {
        return emitterRepository.findAllEventCacheStartWithByMemberId("1");
    }

    @Transactional
    public Map<String, SseEmitter> allEmitter() {
        return emitterRepository.findAllEmittersStartWithUserId("1");
    }

    @Transactional
    public List<ResponseDto> getAllByUserId(Long userId) {
        List<Alarm> alarmList = alarmRepository.findAllByUserId(userId);
        return alarmList.stream().map(ResponseDto::from).toList();
    }

    @Transactional
    public ResponseDto getByAlarmId(Long alarmId) {
        Alarm alarm = alarmRepository.findByAlarmId(alarmId);
        return ResponseDto.from(alarm);
    }

    @Transactional
    public List<ResponseDto> getAll() {
        List<Alarm> alarmList = alarmRepository.findAll();
        return alarmList.stream().map(ResponseDto::from).toList();
    }

    private void createAlarm(Alarm alarm) {
        Alarm savedAlarm = alarmRepository.save(alarm);
        log.info("*** [New Alarm Created : {} ] ***", savedAlarm.getAlarmId());
    }

    private void sendAlarmSSE(Alarm alarm) {
        String id = createSseIdWithCurrentTime(alarm.getUserId());
        emitterRepository.saveEventCache(id, alarm.getTitle() + alarm.getContent());
        log.info("*** [Event Cache Saved] ***");
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmittersStartWithUserId(String.valueOf(alarm.getUserId()));
        emitters.forEach( //사용자 여러 브라우저로 로그인 했을 경우 존재
                (key, emitter) -> {
                    sendToClient(emitter, id, alarm.getTitle() + alarm.getContent());
                }
        );
    }

    private void sendToClient(SseEmitter emitter, String id, Object data) {
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(id)
                        .name("Alarm SSE")
                        .data(data));
                log.info("*** [SSE Successfully Sent to User: {} ] *** ", id);
            } catch (IOException exception) {
                emitterRepository.deleteById(id);
//                emitter.completeWithError(exception);
                log.error("*** [SSE Sent Failed to User {} ]*** \n*** [ Caused by {} ] ***", id, exception.getMessage());
//                throw new RuntimeException("SSE 전송 실패 : 연결 오류");
            }
        }
    }

    private String createSseIdWithCurrentTime(Long userId) {
        return userId + "_" + System.currentTimeMillis();
    }
}
