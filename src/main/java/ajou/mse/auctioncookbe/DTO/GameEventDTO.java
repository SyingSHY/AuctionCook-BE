package ajou.mse.auctioncookbe.DTO;

import ajou.mse.auctioncookbe.entity.GameEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class GameEventDTO {

    private String eventType;
    private LocalDateTime eventTime;
    private String eventSource;
    private String eventTarget;
    private String eventContent;

    public GameEventDTO(String eventType, String eventSource, String eventTarget, String eventContent) {
        this.eventType = eventType;
        this.eventTime = LocalDateTime.now();
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
        this.eventTime = LocalDateTime.now();
    }
}
