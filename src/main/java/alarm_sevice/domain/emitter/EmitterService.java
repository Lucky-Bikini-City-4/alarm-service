package alarm_sevice.domain.emitter;


import alarm_sevice.domain.alarm.Alarm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmitterService {

    private final EmitterRepository emitterRepository;
    private final static Long TIMEOUT = 60L * 1000 * 60;

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

    public void sendAlarmSSE(Alarm alarm) {
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
