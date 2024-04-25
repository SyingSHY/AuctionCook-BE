package ajou.mse.auctioncookbe.repository;

import ajou.mse.auctioncookbe.entity.WaitingRoom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WaitingRoomRepository extends CrudRepository<WaitingRoom, String> {

    Optional<WaitingRoom> findWaitingRoomByRoomCode(String roomCode);
}
