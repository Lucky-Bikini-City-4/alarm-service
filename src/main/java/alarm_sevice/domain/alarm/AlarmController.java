package alarm_sevice.domain.alarm;

import alarm_sevice.auth.Passport;
import alarm_sevice.auth.PassportHolder;
import alarm_sevice.domain.alarm.dto.ResponseDto;
import alarm_sevice.domain.emitter.EmitterService;
import alarm_sevice.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;
    private final EmitterService emitterService;

    /* Kafka Producer test*/
//    @PostMapping("/send-message-queue/1")
//    public String sendMessageQueue(@RequestParam("topic") String topic,
//                                   @RequestParam("key") String key,
//                                   @RequestBody RestaurantBookDto dto) {
//        alarmService.sendMessageQueue1(topic, key, dto);
//        return "Message sent to Kafka topic";
//    }
//
//    @PostMapping("/send-message-queue/2")
//    public String sendMessageQueue(@RequestParam("topic") String topic,
//                                   @RequestParam("key") String key,
//                                   @RequestBody RestaurantBookConfirmDto dto) {
//        alarmService.sendMessageQueue2(topic, key, dto);
//        return "Message sent to Kafka topic";
//    }

    @GetMapping(value = "/subscribe", produces = "text/event-stream;charset=UTF-8")
    public SseEmitter subscribe(
            @RequestParam(value = "Last-Event-Id", required = false, defaultValue = "") String lastEventId,
            @PassportHolder Passport passport
    ) {
        Long userId = passport.userId();
        return emitterService.subscribe(userId, lastEventId);
    }

    @GetMapping("/all-cache")
    public Map<String, Object> allCache() {
        return emitterService.allCache();
    }

    @GetMapping("/all-emitter")
    public Map<String, SseEmitter> allEmitter() {
        return emitterService.allEmitter();
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<ResponseDto>>> getAllByUserId(@PassportHolder Passport passport) {
        Long userId = passport.userId();
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
