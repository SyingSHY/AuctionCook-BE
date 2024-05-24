package ajou.mse.auctioncookbe.service;

import ajou.mse.auctioncookbe.DTO.AuthResponseDTO;
import ajou.mse.auctioncookbe.DTO.WaitingRoomResponseDTO;
import ajou.mse.auctioncookbe.entity.InGameRoom;
import ajou.mse.auctioncookbe.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DummyService {

    @Autowired
    private AuthService authService;
    @Autowired
    private WaitingRoomService waitingRoomService;
    @Autowired
    private GameRoomManageService gameRoomManageService;

    public WaitingRoomResponseDTO putDummyToWaitingRoom(String waitingRoomCode) {

        List<String> dummyList = new ArrayList<>();

        AuthResponseDTO dummyAuth01 = authService.loginUser("Dummy-Bot-01");
        dummyList.add(dummyAuth01.getUserUUID());

        AuthResponseDTO dummyAuth02 = authService.loginUser("Dummy-Bot-02");
        dummyList.add(dummyAuth02.getUserUUID());

        AuthResponseDTO dummyAuth03 = authService.loginUser("Dummy-Bot-03");
        dummyList.add(dummyAuth03.getUserUUID());

        String waitingRoomID = "";
        for (String userUUID : dummyList) {
            WaitingRoomResponseDTO waitingRoomResponseDTO = waitingRoomService.joinRoom(waitingRoomCode, userUUID);
            if (waitingRoomResponseDTO.getResultStatus().equals("FAILED")) {
                return waitingRoomResponseDTO;
            }
            waitingRoomID = waitingRoomResponseDTO.getWaitingRoomInfo().getRedisId();
        }

        for (String userUUID : dummyList) {
            WaitingRoomResponseDTO waitingRoomResponseDTO = waitingRoomService.setReadyState(waitingRoomID, userUUID);
            if (waitingRoomResponseDTO.getResultStatus().equals("FAILED")) {
                return waitingRoomResponseDTO;
            }
        }

        return WaitingRoomResponseDTO.builder()
                .resultStatus("SUCCESS")
                .description("Dummies added to the waiting room.")
                .waitingRoomInfo(null)
                .build();
    }

    public String setDummyToGameReady(String gameRoomID, String humanPlayerID) {

        InGameRoom inGameRoom = gameRoomManageService.queryGameRoom(gameRoomID);
        List<Player> playerList = inGameRoom.getGamePlayers();

        for (Player player : playerList) {
            if (!player.getPlayerID().equals(humanPlayerID)) {
                gameRoomManageService.setReady(gameRoomID, player.getPlayerID());
            }
        }

        return "SUCCESS";
    }
}
