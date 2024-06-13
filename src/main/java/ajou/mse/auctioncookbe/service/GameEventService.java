package ajou.mse.auctioncookbe.service;

import ajou.mse.auctioncookbe.DTO.GameEventDTO;
import ajou.mse.auctioncookbe.entity.*;
import ajou.mse.auctioncookbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class GameEventService {

    public static final int MAX_PLAYER_INGAME = 4;

    private final GameRoomManageService gameRoomManageService;
    private final UserRepository userRepository;

    @Autowired
    public GameEventService(GameRoomManageService gameRoomManageService, UserRepository userRepository) {
        this.gameRoomManageService = gameRoomManageService;
        this.userRepository = userRepository;
    }

    public String postEventByPlayer(String gameID, String playerID, GameEventDTO gameEventDTO) {

        InGameRoom targetGameRoom = gameRoomManageService.queryGameRoom(gameID);
        if (targetGameRoom == null) {
            return "FAILED: No such game room";
        }

        Player player = targetGameRoom.getGamePlayer(playerID);
        if (player == null) {
            return "FAILED: No such player";
        }

        GameEvent gameEvent = new GameEvent(gameEventDTO, targetGameRoom);
        gameEvent.checkEventBy(playerID);
        targetGameRoom.getGameEventBus().add(gameEvent);

        return "SUCCESS: Event posted";
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

        boolean isAdded = targetGameRoom.getGameEventBus().add(gameEvent);

        if (isAdded) return "SUCCESS";
        else return "FAILED";
    }

    public List<GameEventDTO> fetchEvent(String gameID, String playerID) {

        // 반환 이벤트를 실을 리스트 초기화
        List<GameEventDTO> resultEvent = new ArrayList<>();

        InGameRoom targetGameRoom = gameRoomManageService.queryGameRoom(gameID);
        if (targetGameRoom == null) {
            return null;
        }

        Player player = targetGameRoom.getGamePlayer(playerID);
        if (player == null) {
            return null;
        }

        // 이벤트 버스 내 이벤트 수신 처리
        List<GameEvent> targetGameEventBus = targetGameRoom.getGameEventBus();
        synchronized (targetGameEventBus) {
            Iterator<GameEvent> iterator = targetGameEventBus.iterator();
            while (iterator.hasNext()) {
                GameEvent event = iterator.next();
                if (event.isCheckedBy(playerID)) {
                    continue;
                } else {
                    resultEvent.add(new GameEventDTO(event));

                    event.checkEventBy(playerID);
                    if (event.getEventCheckedCount() == MAX_PLAYER_INGAME) {
                        iterator.remove();
                    }
                }
            }
        }

        return resultEvent;
    }
}
