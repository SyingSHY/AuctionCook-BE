package ajou.mse.auctioncookbe.DTO;

import ajou.mse.auctioncookbe.entity.InGameRoom;
import ajou.mse.auctioncookbe.entity.Player;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Getter
public class InGameRoomDTO {
    // Game Phase
    // WAIT: 게임 시작하지 않음
    // READY: BIDDING 준비 - 요리 또는 경매 선택 - 15초 후 시작
    // START: BIDDING 개시 - 요리 완료 후 대기 / 경매 진행 - 30초 후 종료
    // END: BIDDING 종료 - 정산 및 동기화 - 15초 후 READY로 전환
    // QUIT: 게임 종료

    private String gameID;
    private String gameCreator;
    private List<Player> gamePlayers;
    private String gamePhase;
    private String currentTimeStamp;
    private String currentGamePhaseTimeStamp;
    private int gameTurnCount;

    public InGameRoomDTO(InGameRoom inGameRoom) {
        this.gameID = inGameRoom.getGameID();
        this.gameCreator = inGameRoom.getGameCreator();
        this.gamePlayers = inGameRoom.getGamePlayers();
        this.gamePhase = inGameRoom.getGameState().toString();
        this.currentTimeStamp = LocalDateTime.now(ZoneId.of("Asia/Seoul")).toString();
        this.currentGamePhaseTimeStamp = inGameRoom.getCurrentGamePhaseTimeStamp().toString();
        this.gameTurnCount = inGameRoom.getGameTurnCount();
    }
}
