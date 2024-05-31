package ajou.mse.auctioncookbe.entity;

import ajou.mse.auctioncookbe.DTO.GameEventDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class GameEvent {

    private String eventType;
    private LocalDateTime eventTime;
    private String eventSource;
    private String eventTarget;
    private String eventContent;
    private Map<String, Boolean> eventCheckedBy;
    private int eventCheckedCount;

    public GameEvent(String eventType, String eventTarget, String eventContent, InGameRoom gameRoom) {
        this.eventType = eventType;
        this.eventTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        this.eventSource = "SERVER";
        this.eventTarget = eventTarget;
        this.eventContent = eventContent;
        this.eventCheckedBy = new HashMap<>();
        this.eventCheckedCount = 0;

        for (Player player : gameRoom.getGamePlayers()) {
            this.eventCheckedBy.put(player.getPlayerID(), true);
        }
        this.eventCheckedBy.put(eventTarget, false);
    }

    public GameEvent(String eventType, String eventContent, InGameRoom gameRoom) {
        this.eventType = eventType;
        this.eventTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        this.eventSource = "SERVER";
        this.eventTarget = "ALL";
        this.eventContent = eventContent;
        this.eventCheckedBy = new HashMap<>();
        this.eventCheckedCount = 0;

        for (Player player : gameRoom.getGamePlayers()) {
            this.eventCheckedBy.put(player.getPlayerID(), false);
        }
    }

    public GameEvent(GameEventDTO gameEventDTO, InGameRoom gameRoom) {
        this.eventType = gameEventDTO.getEventType();
        this.eventTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        this.eventSource = gameEventDTO.getEventSource();
        this.eventTarget = gameEventDTO.getEventTarget();
        this.eventContent = gameEventDTO.getEventContent();
        this.eventCheckedBy = new HashMap<>();
        this.eventCheckedCount = 0;

        for (Player player : gameRoom.getGamePlayers()) {
            this.eventCheckedBy.put(player.getPlayerID(), false);
        }
    }

    public GameEvent() {
        this.eventType = "NULL";
        this.eventTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        this.eventSource = "NULL";
        this.eventTarget = "NULL";
        this.eventContent = "NULL";
        this.eventCheckedBy = new HashMap<>();
        this.eventCheckedCount = 0;
    }

    public void checkEventBy(String userID) {

        this.eventCheckedBy.replace(userID, true);
        this.eventCheckedCount++;
    }

    public boolean isCheckedBy(String playerID) {
        return this.getEventCheckedBy().get(playerID);
    }
}
