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

    private User roomCreator;

    private boolean gameStart = false;

    private Map<String, User> joinedUsers = new HashMap<String, User>() {{
        put("Player1", null);
        put("Player2", null);
        put("Player3", null);
        put("Player4", null);
    }};

    public WaitingRoom(String roomCode, User roomCreator, User joinedUser) {
        this.roomCode = roomCode;
        this.roomCreator = roomCreator;
        joinUser(joinedUser);
    }

    public User joinUser(User user) {

        for (String userSeat : joinedUsers.keySet()) {
            if (joinedUsers.get(userSeat) == null) {
                return joinedUsers.put(userSeat, user);
            }
        }

        return null;
    }

    public User leaveUser(User user) {

        for (String userSeat : joinedUsers.keySet()) {
            if (joinedUsers.get(userSeat).equals(user)) {
                return joinedUsers.remove(userSeat);
            }
        }

        return null;
    }
}
