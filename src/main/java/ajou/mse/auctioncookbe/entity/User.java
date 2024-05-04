package ajou.mse.auctioncookbe.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "USER")
public class User {

    @Id
    private String redisId;

    @Indexed
    private String name;

    private String joiningRoom;

    private boolean statusGameReady = false;

    private boolean statusLoadingEnd = false;

    @TimeToLive
    private long timeToLive;

    public User updateDataTtl(long timeToLive) {
        this.timeToLive = timeToLive;
        return this;
    }
}
