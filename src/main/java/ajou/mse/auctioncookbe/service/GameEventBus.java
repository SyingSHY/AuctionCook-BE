package ajou.mse.auctioncookbe.service;

import ajou.mse.auctioncookbe.DTO.GameEventDTO;
import ajou.mse.auctioncookbe.entity.GameEvent;
import ajou.mse.auctioncookbe.entity.InGameRoom;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameEventBus {

    public static final int MAX_PLAYER_INGAME = 4;

    private final Map<String, List<GameEvent>> gameEventBusList;

    public GameEventBus() {
        this.gameEventBusList = new ConcurrentHashMap<>();
    }

    public void addGameEventBus(String gameID) {
        List<GameEvent> gameEventBus = Collections.synchronizedList(new ArrayList<>());
        this.gameEventBusList.put(gameID, gameEventBus);
    }

    public void removeGameEventBus(String gameID) {
        this.gameEventBusList.remove(gameID);
    }

    public void postEventByPlayer(String gameID, String playerID, GameEventDTO gameEventDTO, InGameRoom targetGameRoom) {

        List<GameEvent> targetGameEventBus = this.gameEventBusList.get(gameID);

        GameEvent gameEvent = new GameEvent(gameEventDTO, targetGameRoom);
        gameEvent.checkEventBy(playerID);

        targetGameEventBus.add(gameEvent);
    }

    public void postEventByServer(String gameID, String eventType, InGameRoom targetGameRoom) {

        List<GameEvent> targetGameEventBus = this.gameEventBusList.get(gameID);

        GameEvent gameEvent = switch (eventType) {
            case "GAME START" -> new GameEvent(eventType, "Game has started", targetGameRoom);
            case "PHASE READY" -> new GameEvent(eventType, "Phase READY", targetGameRoom);
            case "PHASE START" -> new GameEvent(eventType, "Phase START", targetGameRoom);
            case "PHASE END" -> new GameEvent(eventType, "Phase END", targetGameRoom);
            case "GAME END" -> new GameEvent(eventType, "Game has ended", targetGameRoom);
            default -> new GameEvent();
        };

        targetGameEventBus.add(gameEvent);
    }

    public List<GameEventDTO> fetchEvent(String gameID, String playerID) {

        // 반환 이벤트를 실을 리스트 초기화
        List<GameEventDTO> resultEventList = new ArrayList<>();

        // 이벤트 버스 내 이벤트 수신 처리
        List<GameEvent> targetGameEventBus = this.gameEventBusList.get(gameID);
        synchronized (targetGameEventBus) {
            Iterator<GameEvent> iterator = targetGameEventBus.iterator();
            while (iterator.hasNext()) {
                GameEvent event = iterator.next();
                if (event.isCheckedBy(playerID)) {
                    continue;
                } else {
                    resultEventList.add(new GameEventDTO(event));

                    event.checkEventBy(playerID);
                    if (event.getEventCheckedCount() == MAX_PLAYER_INGAME) {
                        iterator.remove();
                    }
                }
            }
        }

        return resultEventList;
    }
}
