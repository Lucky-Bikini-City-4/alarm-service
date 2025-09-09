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
