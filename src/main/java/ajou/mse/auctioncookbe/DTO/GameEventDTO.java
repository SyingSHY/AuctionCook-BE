package ajou.mse.auctioncookbe.DTO;

import ajou.mse.auctioncookbe.entity.GameEvent;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Builder
@AllArgsConstructor
public class GameEventDTO {

    private String eventType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.nnnnnnnnn", timezone = "Asia/Seoul")
    private LocalDateTime eventTime;
    private String eventSource;
    private String eventTarget;
    private String eventContent;

    public GameEventDTO(String eventType, String eventSource, String eventTarget, String eventContent) {
        this.eventType = eventType;
        this.eventTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        this.eventSource = eventSource;
        this.eventTarget = eventTarget;
        this.eventContent = eventContent;
    }

    public GameEventDTO(GameEvent gameEvent) {
        this.eventType = gameEvent.getEventType();
        this.eventTime = gameEvent.getEventTime();
        this.eventSource = gameEvent.getEventSource();
        this.eventTarget = gameEvent.getEventTarget();
        this.eventContent = gameEvent.getEventContent();
    }

    public void setEventTime() {
        this.eventTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}
