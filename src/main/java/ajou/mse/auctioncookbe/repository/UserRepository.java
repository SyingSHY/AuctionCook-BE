package ajou.mse.auctioncookbe.repository;

import ajou.mse.auctioncookbe.entity.UserData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserData, String> {
}
