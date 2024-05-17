package ajou.mse.auctioncookbe.service;

import ajou.mse.auctioncookbe.entity.*;
import ajou.mse.auctioncookbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class GameRoomManageService {

//    @Autowired
//    private GameEventService gameEventService;
    @Autowired
    private UserRepository userRepository;

    private Map<String, InGameRoom> inGameRooms;

    public GameRoomManageService() {
        this.inGameRooms = new HashMap<>();
    }

    public String initGameRoom(WaitingRoom waitingRoom) {
        List<Player> playerList = fetchJoinedPlayers(waitingRoom);
        InGameRoom inGameRoom = new InGameRoom(waitingRoom, playerList);
        inGameRooms.put(inGameRoom.getGameID(), inGameRoom);

        return "";
    }

    public String setReady(String gameRoomID, String playerID) {

        InGameRoom gameRoom = queryGameRoom(gameRoomID);
        List<Player> playerList = gameRoom.getGamePlayers();
        Player playerOne = gameRoom.getGamePlayer(playerID);

        playerOne.finishLoading();
        for (Player player : playerList) {
            if (!player.isFinishedLoading()) return "NOW YOU'RE READY. KEEP WATCHING EVENT BUS.";
        }

        // gameEventService.postEventByServer(gameRoomID, "GAME START");

        gameRoom.setGamePhase("READY");
        gameRoom.setCurrentGamePhaseTimeStamp(LocalDateTime.now());

        // gameEventService.postEventByServer(gameRoomID, "PHASE READY");

        return "GAME START";
    }

    public String toggleBorC(String gameRoomID, String playerID) {

        InGameRoom gameRoom = queryGameRoom(gameRoomID);
        Player player = gameRoom.getGamePlayer(playerID);

        if (gameRoom.getGamePhase().equals("READY")) {
            boolean nowToggle = player.toggleGoingOnBid();

            return nowToggle ? "You will bid" : "You will cook";
        }
        else return "Not Allowed in other phase";
    }

    public String postBid(String gameRoomID, String playerID, int currentBid, int newBid) {

        InGameRoom gameRoom = queryGameRoom(gameRoomID);
        Player player = gameRoom.getGamePlayer(playerID);

        boolean result = player.putBidding(currentBid, newBid);

        return result + "";
    }

    public String postRecipe(String gameRoomID, String playerID, int recipeID) {

        InGameRoom gameRoom = queryGameRoom(gameRoomID);
        Player player = gameRoom.getGamePlayer(playerID);

        List<Integer> result = player.cookRecipe(recipeID);

        return result.toString();
    }

    public InGameRoom queryGameRoom(String gameRoomID) {
        for (InGameRoom gameRoom : inGameRooms.values()) {
            if (gameRoom.getGameID().equals(gameRoomID)) {
                LocalDateTime roomPhaseTimeStamp = gameRoom.getCurrentGamePhaseTimeStamp();
                switch (gameRoom.getGamePhase()) {
                    case "READY":
                        if (roomPhaseTimeStamp.plusSeconds(15L).isBefore(LocalDateTime.now())) {
                            gameRoom.setGamePhase("START");
                            gameRoom.setCurrentGamePhaseTimeStamp(roomPhaseTimeStamp.plusSeconds(15L));
                        }
                        break;
                    case "START":
                        if (roomPhaseTimeStamp.plusSeconds(30L).isBefore(LocalDateTime.now())) {
                            gameRoom.setGamePhase("END");
                            gameRoom.setCurrentGamePhaseTimeStamp(roomPhaseTimeStamp.plusSeconds(30L));
                            // 토큰 삭감 및 보존 처리 필요
                        }
                        break;
                    case "END":
                        if (roomPhaseTimeStamp.plusSeconds(15L).isBefore(LocalDateTime.now())) {
                            gameRoom.setGamePhase("READY");
                            gameRoom.setCurrentGamePhaseTimeStamp(roomPhaseTimeStamp.plusSeconds(15L));
                            gameRoom.setGameTurnCount(gameRoom.getGameTurnCount() + 1);
                            gameRoom.provideTokenPerTurn();
                        }
                        break;
                    default:
                }
                return gameRoom;
            }
        }
        return null;
    }

    public List<InGameRoom> queryGameRoomList() {
        return inGameRooms.values().stream().toList();
    }

    public boolean removeGameRoom(String gameRoomID) {
        for (InGameRoom gameRoom : inGameRooms.values()) {
            if (gameRoom.getGameID().equals(gameRoomID)) {
                inGameRooms.remove(gameRoom);
                return true;
            }
        }
        return false;
    }

    private List<Player> fetchJoinedPlayers(WaitingRoom waitingRoom) {
        List<Player> result = new ArrayList<>();

        List<String> playersID = waitingRoom.getJoinedUsers();
        for (String playerID : playersID) {
            Player fetchedUser = new Player(userRepository.findById(playerID).get());
            result.add(fetchedUser);
        }

        return result;
    }

    private boolean isPhaseEnd(InGameRoom gameRoom) {
        LocalDateTime gamePhaseTimeStamp = gameRoom.getCurrentGamePhaseTimeStamp();
        LocalDateTime nowTimeStamp = LocalDateTime.now();

        return nowTimeStamp.isAfter(gamePhaseTimeStamp);
    }
}
