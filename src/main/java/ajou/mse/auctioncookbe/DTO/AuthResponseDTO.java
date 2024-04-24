package ajou.mse.auctioncookbe.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponseDTO {

    private String resultStatus;
    private String description;
    private String userUUID;
    private String name;
}
