package ajou.mse.auctioncookbe.DTO;

import ajou.mse.auctioncookbe.entity.WaitingRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaitingRoomResponseDTO {

    private String resultStatus;
    private String description;
    private WaitingRoomDTO waitingRoomInfo;
}
