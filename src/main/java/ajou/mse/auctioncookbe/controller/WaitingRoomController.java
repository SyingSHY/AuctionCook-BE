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

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<WaitingRoomResponseDTO> checkRoom(@PathVariable String roomId, @RequestParam String userUUID) {
        // Check room status
        // -----------------
        // 방의 상태 확인(게임 준비 여부)

        WaitingRoomResponseDTO waitingRoomResponseDTO = waitingRoomService.checkRoom(roomId, userUUID);

        if (waitingRoomResponseDTO.getResultStatus().equals("SUCCESS"))
            return ResponseEntity.ok(waitingRoomResponseDTO);
        else return ResponseEntity.badRequest().body(waitingRoomResponseDTO);
    }

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
    public ResponseEntity<WaitingRoomResponseDTO> leaveRoom(@PathVariable String roomId, @RequestParam String userUUID) {
        // Suggest RoomID and left the waiting room
        // --------------------------------
        // 현재 대기실의 RoomID와 자신의 UUID를 제시하고 이탈

        WaitingRoomResponseDTO waitingRoomResponseDTO = waitingRoomService.leaveRoom(roomId, userUUID);

        if (waitingRoomResponseDTO.getResultStatus().equals("SUCCESS"))
            return ResponseEntity.ok(waitingRoomResponseDTO);
        else return ResponseEntity.badRequest().body(waitingRoomResponseDTO);
    }

    @PutMapping("/rooms/{roomId}")
    public ResponseEntity<WaitingRoomResponseDTO> setReadyState(@PathVariable String roomId, @RequestParam String userUUID) {
        //
        // ----------------------------------
        // 현재 대기실 내 본인의 게임 준비 상태 변경

        WaitingRoomResponseDTO waitingRoomResponseDTO = waitingRoomService.setReadyState(roomId, userUUID);

        if (waitingRoomResponseDTO.getResultStatus().equals("SUCCESS"))
            return ResponseEntity.ok(waitingRoomResponseDTO);
        else return ResponseEntity.badRequest().body(waitingRoomResponseDTO);
    }
}
