package ajou.mse.auctioncookbe.service;

import ajou.mse.auctioncookbe.entity.*;
import ajou.mse.auctioncookbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class GameEventService {

    public static final int MAX_PLAYER_INGAME = 4;
    @Autowired
    private GameRoomManageService gameRoomManageService;
    @Autowired
    private UserRepository userRepository;

    public String postEventByPlayer(String gameID, String playerID, IncomingEvent incomingEvent) {

        InGameRoom targetGameRoom = gameRoomManageService.queryGameRoom(gameID);
        if (targetGameRoom == null) {
            return "FAILED";
        }

        Player player = targetGameRoom.getGamePlayer(playerID);
        if (player == null) {
            return "FAILED";
        }

        GameEvent gameEvent = new GameEvent(incomingEvent, targetGameRoom);
        gameEvent.checkEventBy(playerID);
        targetGameRoom.getGameEventBus().offer(gameEvent);

        return "SUCCESS";
    }

    public String postEventByServer(String gameID, String eventType) {

        InGameRoom targetGameRoom = gameRoomManageService.queryGameRoom(gameID);
        if (targetGameRoom == null) {
            return "FAILED";
        }

        GameEvent gameEvent = switch (eventType) {
            case "GAME START" -> new GameEvent(eventType, "Game has started", targetGameRoom);
            case "PHASE READY" -> new GameEvent(eventType, "Phase READY", targetGameRoom);
            case "PHASE START" -> new GameEvent(eventType, "Phase START", targetGameRoom);
            case "PHASE END" -> new GameEvent(eventType, "Phase END", targetGameRoom);
            case "GAME END" -> new GameEvent(eventType, "Game has ended", targetGameRoom);
            default -> new GameEvent();
        };

        boolean isAdded = targetGameRoom.getGameEventBus().offerLast(gameEvent);

        if (isAdded) return "SUCCESS";
        else return "FAILED";
    }

    public List<IncomingEvent> fetchEvent(String gameID, String playerID) {

        // 반환 이벤트를 실을 리스트 초기화
        List<IncomingEvent> resultEvent = new ArrayList<>();

        InGameRoom targetGameRoom = gameRoomManageService.queryGameRoom(gameID);
        if (targetGameRoom == null) {
            return null;
        }

        Player player = targetGameRoom.getGamePlayer(playerID);
        if (player == null) {
            return null;
        }

        LinkedList<GameEvent> targetGameEventBus = targetGameRoom.getGameEventBus();
        for (GameEvent event : targetGameEventBus) {
            if (event.isCheckedBy(playerID)) {
                continue;
            }
            else {
                resultEvent.add(new IncomingEvent(event));

                event.checkEventBy(playerID);
                if (event.getEventCheckedCount() == MAX_PLAYER_INGAME) {
                    targetGameEventBus.pollFirst();
                }
            }
        }

        return resultEvent;
    }
}
