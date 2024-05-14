package ajou.mse.auctioncookbe.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IncomingEvent {

    private String eventType;
    private String eventSource;
    private String eventTarget;
    private String eventContent;

    public IncomingEvent(GameEvent gameEvent) {
        this.eventType = gameEvent.getEventType();
        this.eventSource = gameEvent.getEventSource();
        this.eventTarget = gameEvent.getEventTarget();
        this.eventContent = gameEvent.getEventContent();
    }
}
