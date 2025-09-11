package alarm_sevice.domain.alarm;

import alarm_sevice.domain.alarm.dto.CreateRequestDto;
import alarm_sevice.domain.alarm.dto.ResponseDto;
import alarm_sevice.domain.alarm.kafkaDto.booking.RestaurantBookConfirmDto;
import alarm_sevice.domain.alarm.kafkaDto.booking.RestaurantBookDto;
import alarm_sevice.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    /* Kafka Producer */
    @PostMapping("/send-message-queue/1")
    public String sendMessageQueue(@RequestParam("topic") String topic,
                              @RequestParam("key") String key,
                              @RequestBody RestaurantBookDto dto) {
        alarmService.sendMessageQueue1(topic, key, dto);
        return "Message sent to Kafka topic";
    }

    @PostMapping("/send-message-queue/2")
    public String sendMessageQueue(@RequestParam("topic") String topic,
                                   @RequestParam("key") String key,
                                   @RequestBody RestaurantBookConfirmDto dto) {
        alarmService.sendMessageQueue2(topic, key, dto);
        return "Message sent to Kafka topic";
    }

    @GetMapping(value = "/subscribe", produces = "text/event-stream;charset=UTF-8")
    public SseEmitter subscribe(){
        Long userId = 1L;
        return alarmService.subscribe(userId);
    }


    /* 헤더에서 추출한 사용자의 모든 알람 조회 */
    @GetMapping()
    public ResponseEntity<ApiResponse<List<ResponseDto>>> getAllByUserId() {
        Long userId = 1L;
        List<ResponseDto> response = alarmService.getAllByUserId(userId);
        return ApiResponse.success(HttpStatus.OK, response);
    }

    /* 특정 알람 조회 */
    @GetMapping("/{alarmId}")
    public ResponseEntity<ApiResponse<ResponseDto>> getByAlarmId(@PathVariable(name = "alarmId") Long alarmId) {
        ResponseDto response = alarmService.getByAlarmId(alarmId);
        return ApiResponse.success(HttpStatus.OK, response);
    }

    /* 그냥 모든 알람 조회 */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ResponseDto>>> getAll() {
        Long userId = 1L;
        List<ResponseDto> response = alarmService.getAll();
        return ApiResponse.success(HttpStatus.OK, response);
    }

}
