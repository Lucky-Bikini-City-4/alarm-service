package alarm_sevice.domain.alarm;

import alarm_sevice.domain.alarm.dto.CreateRequestDto;
import alarm_sevice.domain.alarm.dto.ResponseDto;
import alarm_sevice.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

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

    /* 테스트를 위한 임의 메서드 */
    @PostMapping()
    public ResponseEntity<ApiResponse<ResponseDto>> create(@RequestBody CreateRequestDto requestDto) {
        ResponseDto response = alarmService.create(requestDto);
        return ApiResponse.success(HttpStatus.OK, "생성 완료", response);
    }


}
