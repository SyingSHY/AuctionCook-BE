package ajou.mse.auctioncookbe.controller;

import ajou.mse.auctioncookbe.DTO.GameEventDTO;
import ajou.mse.auctioncookbe.entity.IncomingEvent;
import ajou.mse.auctioncookbe.service.GameEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GameEventController {

    @Autowired
    private GameEventService gameEventService;

    @GetMapping("/games/{gameID}/events")
    public List<GameEventDTO> fetchEvent(@PathVariable String gameID, @RequestParam String userID) {
        // 이벤트 버스에 저장된 이벤트 확인 후 수거

        return gameEventService.fetchEvent(gameID, userID);
    }

    @PostMapping("/games/{gameID}/events")
    public ResponseEntity<String> postEvent(@PathVariable String gameID, @RequestParam String userID, @RequestBody GameEventDTO event) {

        String result = gameEventService.postEventByPlayer(gameID, userID, event);

        if (result.equals("SUCCESS")) return ResponseEntity.ok(result);
        else return ResponseEntity.badRequest().body(result);
    }
}
