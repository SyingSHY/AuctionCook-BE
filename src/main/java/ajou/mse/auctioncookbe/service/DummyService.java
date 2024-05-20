package ajou.mse.auctioncookbe.service;

import ajou.mse.auctioncookbe.DTO.AuthResponseDTO;
import ajou.mse.auctioncookbe.DTO.WaitingRoomResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DummyService {

    @Autowired
    private AuthService authService;
    @Autowired
    private WaitingRoomService waitingRoomService;

    public String putDummyToWaitingRoom(String waitingRoomID) {

        List<String> dummyList = new ArrayList<>();

        AuthResponseDTO dummyAuth01 = authService.loginUser("Dummy-Bot-01");
        dummyList.add(dummyAuth01.getUserUUID());

        AuthResponseDTO dummyAuth02 = authService.loginUser("Dummy-Bot-02");
        dummyList.add(dummyAuth02.getUserUUID());

        AuthResponseDTO dummyAuth03 = authService.loginUser("Dummy-Bot-03");
        dummyList.add(dummyAuth03.getUserUUID());

        for (String userUUID : dummyList) {
            WaitingRoomResponseDTO waitingRoomResponseDTO = waitingRoomService.joinRoom(waitingRoomID, userUUID);
            if (waitingRoomResponseDTO.getResultStatus().equals("FAILED")) {
                return "Failed to add dummies to the waiting room.";
            }
        }

        for (String userUUID : dummyList) {
            WaitingRoomResponseDTO waitingRoomResponseDTO = waitingRoomService.setReadyState(waitingRoomID, userUUID);
            if (waitingRoomResponseDTO.getResultStatus().equals("FAILED")) {
                return "Failed during setting ready state of dummies.";
            }
        }

        return "Successfully added dummies to the waiting room.";
    }
}
