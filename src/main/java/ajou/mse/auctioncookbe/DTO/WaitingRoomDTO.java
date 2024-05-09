package ajou.mse.auctioncookbe.DTO;

import ajou.mse.auctioncookbe.entity.User;
import ajou.mse.auctioncookbe.entity.WaitingRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaitingRoomDTO {

    private String redisId;

    private String roomCode;

    private User roomCreator;

    private boolean gameStart;

    private List<User> joinedUsers;

    public WaitingRoomDTO(WaitingRoom waitingRoom, User roomCreator, List<User> joinedUsers) {
        this.redisId = waitingRoom.getRedisId();
        this.roomCode = waitingRoom.getRoomCode();
        this.roomCreator = roomCreator;
        this.gameStart = waitingRoom.isGameStart();
        this.joinedUsers = joinedUsers;
    }
}
