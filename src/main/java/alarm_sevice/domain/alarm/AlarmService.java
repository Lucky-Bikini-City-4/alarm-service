package alarm_sevice.domain.alarm;

import alarm_sevice.domain.alarm.dto.ResponseDto;
import alarm_sevice.domain.emitter.EmitterRepository;
import alarm_sevice.domain.emitter.EmitterService;
import alarm_sevice.kafka.backoffice.BackofficeRegisterDto;
import alarm_sevice.kafka.booking.*;
import alarm_sevice.kafka.register.ServiceRegisterRequestDto;
import alarm_sevice.kafka.waiting.CustomerFromSellerCancelDto;
import alarm_sevice.kafka.waiting.CustomerFromSellerDto;
import alarm_sevice.kafka.waiting.CustomerWaitingDto;
import alarm_sevice.kafka.waiting.SellerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final EmitterService emitterService;


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
        emitterService.sendAlarmSSE(alarm);
    }

}
