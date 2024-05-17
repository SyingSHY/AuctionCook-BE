package ajou.mse.auctioncookbe.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
public class InGameRoom {
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
    private LocalDateTime currentGamePhaseTimeStamp;
    private int gameTurnCount;
    private LinkedList<GameEvent> gameEventBus;

    public InGameRoom(WaitingRoom waitingRoom, List<Player> gamePlayers) {
        this.gameID = waitingRoom.getRedisId();
        this.gameCreator = waitingRoom.getRoomCreator();

        this.gamePlayers = gamePlayers;
        this.gamePhase = "WAIT";
        this.currentGamePhaseTimeStamp = LocalDateTime.now();
        this.gameTurnCount = 0;
        this.gameEventBus = new LinkedList<>();
    }

    public Player getGamePlayer(String playerID) {
        for (Player playerCandidate : gamePlayers) {
            if (playerCandidate.getPlayerID().equals(playerID)) {
                return playerCandidate;
            }
        }
        return null;
    }

    public void provideTokenPerTurn() {
        for (Player playerCandidate : gamePlayers) {
            playerCandidate.setToken(playerCandidate.getToken() + 30);
        }
    }
}