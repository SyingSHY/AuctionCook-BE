package ajou.mse.auctioncookbe.service;

import ajou.mse.auctioncookbe.DTO.GameEventDTO;
import ajou.mse.auctioncookbe.entity.*;
import ajou.mse.auctioncookbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameEventService {

    private final GameRoomManageService gameRoomManageService;
    private final UserRepository userRepository;
    private final GameEventBus gameEventBus;

    @Autowired
    public GameEventService(GameRoomManageService gameRoomManageService, UserRepository userRepository, GameEventBus gameEventBus) {
        this.gameRoomManageService = gameRoomManageService;
        this.userRepository = userRepository;
        this.gameEventBus = gameEventBus;
    }

    public String postEvent(String gameID, String playerID, GameEventDTO gameEventDTO) {

        InGameRoom targetGameRoom = gameRoomManageService.queryGameRoom(gameID);
        if (targetGameRoom == null) {
            return "FAILED: No such game room";
        }

        Player player = targetGameRoom.getGamePlayer(playerID);
        if (player == null) {
            return "FAILED: No such player";
        }

        gameEventBus.postEventByPlayer(gameID, playerID, gameEventDTO, targetGameRoom);

        return "SUCCESS: Event posted";
    }

    public String postEventByServer(String gameID, String eventType) {

        InGameRoom targetGameRoom = gameRoomManageService.queryGameRoom(gameID);
        if (targetGameRoom == null) {
            return "FAILED";
        }

        gameEventBus.postEventByServer(gameID, eventType, targetGameRoom);

        return "OK";
    }

    public List<GameEventDTO> fetchEvent(String gameID, String playerID) {

        InGameRoom targetGameRoom = gameRoomManageService.queryGameRoom(gameID);
        if (targetGameRoom == null) {
            return null;
        }

        Player player = targetGameRoom.getGamePlayer(playerID);
        if (player == null) {
            return null;
        }

        return gameEventBus.fetchEvent(gameID, playerID);
    }
}
