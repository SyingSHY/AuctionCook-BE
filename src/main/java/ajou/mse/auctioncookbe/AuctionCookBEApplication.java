package ajou.mse.auctioncookbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories
public class AuctionCookBEApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuctionCookBEApplication.class, args);
	}

}
