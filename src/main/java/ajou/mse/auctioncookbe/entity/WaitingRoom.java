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

    private List<User> joinedUsers;

    public WaitingRoom(String roomCode, User roomCreator, User joinedUser) {
        this.roomCode = roomCode;
        this.roomCreator = roomCreator;
        this.joinedUsers.add(joinedUser);
    }

    public boolean joinUser(User user) {
        return joinedUsers.add(user);
    }

    public boolean leaveUser(User user) {
        return joinedUsers.remove(user);
    }
}
