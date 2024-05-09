package ajou.mse.auctioncookbe.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "ROOM")
public class WaitingRoom {

    @Id
    private String redisId;

    @Indexed
    private String roomCode;

    private String roomCreator;

    private boolean gameStart = false;

    private List<String> joinedUsers;

    public WaitingRoom(String roomCode, String roomCreator, String joinedUser) {
        this.roomCode = roomCode;
        this.roomCreator = roomCreator;
        this.joinedUsers.add(joinedUser);
    }

    public boolean joinUser(String user) {
        return joinedUsers.add(user);
    }

    public boolean leaveUser(String user) {
        return joinedUsers.remove(user);
    }
}
