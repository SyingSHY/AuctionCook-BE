package ajou.mse.auctioncookbe.controller;

import ajou.mse.auctioncookbe.DTO.WaitingRoomResponseDTO;
import ajou.mse.auctioncookbe.service.WaitingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class WaitingRoomController {

    @Autowired
    private WaitingRoomService waitingRoomService;

    @PostMapping("/rooms/{roomCode}")
    public ResponseEntity<WaitingRoomResponseDTO> joinRoom(@PathVariable String roomCode, @RequestParam String userUUID) {
        // Enter the waiting room according to RoomID that client provided
        // -------------------------
        // 제시한 RoomID의 대기실 입장

        WaitingRoomResponseDTO waitingRoomResponseDTO = waitingRoomService.joinRoom(roomCode, userUUID);

        if (waitingRoomResponseDTO.getResultStatus().equals("SUCCESS"))
            return ResponseEntity.ok(waitingRoomResponseDTO);
        else return ResponseEntity.badRequest().body(waitingRoomResponseDTO);
    }

    @PostMapping("/rooms")
    public ResponseEntity<WaitingRoomResponseDTO> createRoom(@RequestParam String userUUID) {
        // Create new waiting room
        // -----------------
        // 새로운 대기실 생성

        WaitingRoomResponseDTO waitingRoomResponseDTO = waitingRoomService.createRoom(userUUID);

        if (waitingRoomResponseDTO.getResultStatus().equals("SUCCESS"))
            return ResponseEntity.ok(waitingRoomResponseDTO);
        else return ResponseEntity.badRequest().body(waitingRoomResponseDTO);
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable String roomId) {
        // Suggest RoomID and left the waiting room
        // --------------------------------
        // 현재 대기실의 RoomID를 제시하고 이탈

        return null;
    }
}
