package ajou.mse.auctioncookbe.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Data
@Builder
@RedisHash(value = "USER")
public class UserData {

    @Id
    private String redisId;

    @Indexed
    private String name;

    @TimeToLive
    private long timeToLive;

    public UserData updateDataTtl(long timeToLive) {
        this.timeToLive = timeToLive;
        return this;
    }
}
