package ajou.mse.auctioncookbe.service;

import ajou.mse.auctioncookbe.DTO.WaitingRoomDTO;
import ajou.mse.auctioncookbe.DTO.WaitingRoomResponseDTO;
import ajou.mse.auctioncookbe.entity.User;
import ajou.mse.auctioncookbe.entity.WaitingRoom;
import ajou.mse.auctioncookbe.repository.UserRepository;
import ajou.mse.auctioncookbe.repository.WaitingRoomRepository;
import io.lettuce.core.RedisException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class WaitingRoomService {

    private static final int MAX_USER = 4;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WaitingRoomRepository waitingRoomRepository;
    @Autowired
    private GameRoomManageService gameRoomManageService;

    public WaitingRoomResponseDTO joinRoom(String roomCode, String userID) {
        // Room Check and User Check
        Optional<User> userOptional = userRepository.findById(userID);

        if (userOptional.isEmpty()) {
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("Failed to join the waiting room: Invalid User data.")
                    .waitingRoomInfo(null)
                    .build();
        }

        Optional<WaitingRoom> waitingRoomOptional = waitingRoomRepository.findWaitingRoomByRoomCode(roomCode);

        if (waitingRoomOptional.isEmpty()) {
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("Failed to join the waiting room: Invalid Room data.")
                    .waitingRoomInfo(null)
                    .build();
        }

        // Every parameter is fine. Proceed joining.
        User user = userOptional.get();
        WaitingRoom joiningRoom = waitingRoomOptional.get();

        // Is this user already joining other room?
        if (isJoinedWaitingRoom(user)) {
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("Failed to join a waiting room: User already joining room - " + user.getJoiningRoom())
                    .waitingRoomInfo(waitingRoomDtoConverter(waitingRoomRepository.findById(user.getJoiningRoom()).get()))
                    .build();
        }

        // Is room is full?
        if (isRoomFull(joiningRoom)) {
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("Failed to join the waiting room: Room is full.")
                    .waitingRoomInfo(null)
                    .build();
        }

        joiningRoom.joinUser(user.getRedisId());
        user.setJoiningRoom(joiningRoom.getRedisId());

        // User joins the room
        try {
            userRepository.save(user);
            waitingRoomRepository.save(joiningRoom);
        } catch (RedisException e) {
            e.printStackTrace();
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("Failed to join the waiting room: Redis error.")
                    .waitingRoomInfo(null)
                    .build();
        }

        return WaitingRoomResponseDTO.builder()
                .resultStatus("SUCCESS")
                .description("Successfully joined the waiting room.")
                .waitingRoomInfo(waitingRoomDtoConverter(joiningRoom))
                .build();
    }

    @Transactional
    public WaitingRoomResponseDTO createRoom(String createrID) {

        Optional<User> userOptional = userRepository.findById(createrID);

        if (userOptional.isEmpty()) {
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("Failed to create a waiting room: Invalid User data.")
                    .waitingRoomInfo(null)
                    .build();
        }

        User creatorUser = userOptional.get();

        if (isJoinedWaitingRoom(creatorUser)) {
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("Failed to create a waiting room: User already joining room - " + creatorUser.getJoiningRoom())
                    .waitingRoomInfo(waitingRoomDtoConverter(waitingRoomRepository.findById(creatorUser.getJoiningRoom()).get()))
                    .build();
        }

        WaitingRoom newWaitingRoom = WaitingRoom.builder()
                .redisId(null)
                .roomCode(generateRoomCode())
                .roomCreator(creatorUser.getRedisId())
                .joinedUsers(new ArrayList<>())
                .build();
        WaitingRoom createdWaitingRoom;

        try {
            createdWaitingRoom = waitingRoomRepository.save(newWaitingRoom);
        } catch (RedisException e) {
            e.printStackTrace();
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("Failed to create the waiting room: Redis error.")
                    .waitingRoomInfo(null)
                    .build();
        }

        createdWaitingRoom.joinUser(creatorUser.getRedisId());
        creatorUser.setJoiningRoom(createdWaitingRoom.getRedisId());

        try {
            userRepository.save(creatorUser);
            waitingRoomRepository.save(createdWaitingRoom);
        } catch (RedisException e) {
            e.printStackTrace();
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("Failed to create the waiting room: Redis error.")
                    .waitingRoomInfo(null)
                    .build();
        }

        return WaitingRoomResponseDTO.builder()
                .resultStatus("SUCCESS")
                .description("Successfully created and joined the waiting room.")
                .waitingRoomInfo(waitingRoomDtoConverter(createdWaitingRoom))
                .build();
    }

    public WaitingRoomResponseDTO leaveRoom(String roomID, String userID) {
        // Room Check and User Check
        Optional<User> userOptional = userRepository.findById(userID);

        if (userOptional.isEmpty()) {
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("Failed to leave the waiting room: Invalid User data.")
                    .waitingRoomInfo(null)
                    .build();
        }

        Optional<WaitingRoom> waitingRoomOptional = waitingRoomRepository.findById(roomID);

        if (waitingRoomOptional.isEmpty()) {
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("Failed to leave the waiting room: Invalid Room data.")
                    .waitingRoomInfo(null)
                    .build();
        }

        // Every parameter is fine. Proceed leaving.
        User user = userOptional.get();
        WaitingRoom leavingRoom = waitingRoomOptional.get();

        // Is this user already leaved room?
        if (!isJoinedWaitingRoom(user)) {
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("Failed to leave a waiting room: User already leaved a room.")
                    .waitingRoomInfo(null)
                    .build();
        }

        leavingRoom.leaveUser(user.getRedisId());
        user.setJoiningRoom(null);

        // User leaves the room
        try {
            userRepository.save(user);
            waitingRoomRepository.save(leavingRoom);
        } catch (RedisException e) {
            e.printStackTrace();
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("Failed to leave the waiting room: Redis error.")
                    .waitingRoomInfo(null)
                    .build();
        }

        if (leavingRoom.getJoinedUsers().isEmpty()) {
            try {
                waitingRoomRepository.delete(leavingRoom);
            } catch (RedisException e) {
                e.printStackTrace();
                return WaitingRoomResponseDTO.builder()
                        .resultStatus("FAILED")
                        .description("Failed to leave the waiting room: Redis error.")
                        .waitingRoomInfo(null)
                        .build();
            }
        }

        return WaitingRoomResponseDTO.builder()
                .resultStatus("SUCCESS")
                .description("Successfully leaved the waiting room.")
                .waitingRoomInfo(null)
                .build();
    }

    public WaitingRoomResponseDTO checkRoom(String roomID, String userID) {

        Optional<WaitingRoom> waitingRoomOptional = waitingRoomRepository.findById(roomID);

        WaitingRoom roomCandidate;
        if (waitingRoomOptional.isPresent()) {
            roomCandidate = waitingRoomOptional.get();
        }
        else {
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("There's no such room there.")
                    .waitingRoomInfo(null)
                    .build();
        }

        return WaitingRoomResponseDTO.builder()
                .resultStatus("SUCCESS")
                .description("Returning information of the waiting room.")
                .waitingRoomInfo(waitingRoomDtoConverter(roomCandidate))
                .build();
    }

    public WaitingRoomResponseDTO setReadyState(String roomID, String userID) {
        // Room Check and User Check
        Optional<User> userOptional = userRepository.findById(userID);

        if (userOptional.isEmpty()) {
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("Failed to set ready state: Invalid User data.")
                    .waitingRoomInfo(null)
                    .build();
        }

        Optional<WaitingRoom> waitingRoomOptional = waitingRoomRepository.findById(roomID);

        if (waitingRoomOptional.isEmpty()) {
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("Failed to set ready state: Invalid Room data.")
                    .waitingRoomInfo(null)
                    .build();
        }

        // Every parameter is fine. Proceed.
        User user = userOptional.get();
        WaitingRoom playingRoom = waitingRoomOptional.get();

        if (!isJoinedWaitingRoom(user)) {
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("Failed to set ready state: User is now outside of the room.")
                    .waitingRoomInfo(null)
                    .build();
        }

        // Is request fine?
        if (!Objects.equals(user.getJoiningRoom(), playingRoom.getRedisId())) {
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("Failed to set ready state: There's error in roomID.")
                    .waitingRoomInfo(waitingRoomDtoConverter(playingRoom))
                    .build();
        }

        // Request all fine. set ready status.
        user.setStatusGameReady(!user.isStatusGameReady());

        // User ready
        try {
            userRepository.save(user);
            waitingRoomRepository.save(playingRoom);
        } catch (RedisException e) {
            e.printStackTrace();
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("Failed to set ready state: Redis error.")
                    .waitingRoomInfo(waitingRoomDtoConverter(playingRoom))
                    .build();
        }

        // Re-fetch room information
        Optional<WaitingRoom> waitingRoomOptionalAfter = waitingRoomRepository.findById(roomID);

        playingRoom = waitingRoomOptionalAfter.get();

        if (playingRoom.getJoinedUsers().size() != 4) {

            return WaitingRoomResponseDTO.builder()
                    .resultStatus("SUCCESS")
                    .description("User's ready state changed.")
                    .waitingRoomInfo(waitingRoomDtoConverter(playingRoom))
                    .build();
        }

        for (String playerID : playingRoom.getJoinedUsers()) {
            // Do all players has ready?
            if (!userRepository.findById(playerID).get().isStatusGameReady()) {
                return WaitingRoomResponseDTO.builder()
                        .resultStatus("SUCCESS")
                        .description("User's ready state changed.")
                        .waitingRoomInfo(waitingRoomDtoConverter(playingRoom))
                        .build();
            }
        }

        // Every User now ready
        playingRoom.setGameStart(true);
        try {
            waitingRoomRepository.save(playingRoom);
        } catch (RedisException e) {
            e.printStackTrace();
            return WaitingRoomResponseDTO.builder()
                    .resultStatus("FAILED")
                    .description("Failed to set ready state: Redis error.")
                    .waitingRoomInfo(null)
                    .build();
        }

        gameRoomManageService.initGameRoom(playingRoom);

        return WaitingRoomResponseDTO.builder()
                .resultStatus("SUCCESS")
                .description("User's ready state changed, and all ready to play.")
                .waitingRoomInfo(waitingRoomDtoConverter(playingRoom))
                .build();
    }

    private String generateRoomCode() {

        while (true) {
            StringBuilder roomCodeCandidate = new StringBuilder();
            Random random = new Random();

            for (int i = 0; i < 6; i++) {
                int idx = random.nextInt(10);
                roomCodeCandidate.append(idx);
            }

            // if (isUniqueRoomCode(roomCodeCandidate))
            return roomCodeCandidate.toString();
        }
    }

    private static boolean isJoinedWaitingRoom(User user) {
        return user.getJoiningRoom() != null;
    }

    private boolean isUniqueRoomCode(StringBuilder roomCodeCandidate) {
        return waitingRoomRepository.findWaitingRoomByRoomCode(roomCodeCandidate.toString()).isPresent();
    }

    private static boolean isRoomFull(WaitingRoom joiningRoom) {
        return joiningRoom.getJoinedUsers().size() == MAX_USER;
    }

    private WaitingRoomDTO waitingRoomDtoConverter(WaitingRoom waitingRoom) {
        return new WaitingRoomDTO(waitingRoom,
                fetchRoomCreator(waitingRoom),
                fetchJoinedUsers(waitingRoom));
    }

    private User fetchRoomCreator(WaitingRoom waitingRoom) {
        return userRepository.findById(waitingRoom.getRoomCreator()).get();
    }

    private List<User> fetchJoinedUsers(WaitingRoom waitingRoom) {
        List<User> result = new ArrayList<>();

        List<String> usersID = waitingRoom.getJoinedUsers();
        for (String userID : usersID) {
            User fetchedUser = userRepository.findById(userID).get();
            result.add(fetchedUser);
        }

        return result;
    }
}
