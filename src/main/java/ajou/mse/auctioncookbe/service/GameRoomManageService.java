package ajou.mse.auctioncookbe.service;

import ajou.mse.auctioncookbe.entity.*;
import ajou.mse.auctioncookbe.entity.state.GameStateEnd;
import ajou.mse.auctioncookbe.entity.state.GameStateReady;
import ajou.mse.auctioncookbe.entity.state.GameStateStart;
import ajou.mse.auctioncookbe.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameRoomManageService {

    // private final GameEventService gameEventService;
    private final UserRepository userRepository;

    private Map<String, InGameRoom> inGameRooms;

    @Autowired
    public GameRoomManageService(UserRepository userRepository) {
        this.userRepository = userRepository;
        // this.gameEventService = gameEventService;
    }

    @PostConstruct
    public void init() {
        this.inGameRooms = new ConcurrentHashMap<>();
    }

    public void initGameRoom(WaitingRoom waitingRoom) {
        List<Player> playerList = fetchJoinedPlayers(waitingRoom);
        InGameRoom inGameRoom = new InGameRoom(waitingRoom, playerList);
        inGameRooms.put(inGameRoom.getGameID(), inGameRoom);
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

        gameRoom.moveNextGameState(null);
        gameRoom.setCurrentGamePhaseTimeStamp(LocalDateTime.now());

        // gameEventService.postEventByServer(gameRoomID, "PHASE READY");

        return "GAME START";
    }

    public String toggleBorC(String gameRoomID, String playerID) {

        InGameRoom gameRoom = queryGameRoom(gameRoomID);
        Player player = gameRoom.getGamePlayer(playerID);

        if (gameRoom.getGameState().equals("READY")) {
            boolean nowToggle = player.toggleGoingOnBid();

            return nowToggle ? "You will bid" : "You will cook";
        }
        else return "Not Allowed in other phase";
    }

    public String postBid(String gameRoomID, String playerID, int currentBid, int newBid) {

        InGameRoom gameRoom = queryGameRoom(gameRoomID);
        Player player = gameRoom.getGamePlayer(playerID);

        return gameRoom.getGameState().postBid(playerID, currentBid, newBid);
    }

    public String postRecipe(String gameRoomID, String playerID, int recipeID) {

        InGameRoom gameRoom = queryGameRoom(gameRoomID);
        Player player = gameRoom.getGamePlayer(playerID);

        return gameRoom.getGameState().postRecipe(playerID, recipeID);
    }

    public InGameRoom queryGameRoom(String gameRoomID) {
        for (InGameRoom gameRoom : inGameRooms.values()) {
            if (gameRoom.getGameID().equals(gameRoomID)) {
                LocalDateTime roomPhaseTimeStamp = gameRoom.getCurrentGamePhaseTimeStamp();
                switch (gameRoom.getGameState()) {
                    case GameStateReady ignore -> {
                        if (roomPhaseTimeStamp.plusSeconds(15L).isBefore(LocalDateTime.now(ZoneId.of("Asia/Seoul")))) {
                            gameRoom.moveNextGameState(null);
                            gameRoom.setCurrentGamePhaseTimeStamp(roomPhaseTimeStamp.plusSeconds(15L));
                            // gameEventService.postEventByServer(gameRoomID, "PHASE START");
                        }
                    }
                    case GameStateStart ignore -> {
                        if (roomPhaseTimeStamp.plusSeconds(30L).isBefore(LocalDateTime.now(ZoneId.of("Asia/Seoul")))) {
                            gameRoom.moveNextGameState(null);
                            gameRoom.setCurrentGamePhaseTimeStamp(roomPhaseTimeStamp.plusSeconds(30L));
                            // gameEventService.postEventByServer(gameRoomID, "PHASE END");
                        }
                    }
                    case GameStateEnd ignore -> {
                        if (roomPhaseTimeStamp.plusSeconds(15L).isBefore(LocalDateTime.now(ZoneId.of("Asia/Seoul")))) {
                            gameRoom.moveNextGameState(null);
                            gameRoom.setCurrentGamePhaseTimeStamp(roomPhaseTimeStamp.plusSeconds(15L));
                            gameRoom.setGameTurnCount(gameRoom.getGameTurnCount() + 1);
                            gameRoom.provideTokenPerTurn();
                            // gameEventService.postEventByServer(gameRoomID, "PHASE READY");
                        }
                    }
                    default -> {
                        continue;
                    }
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
