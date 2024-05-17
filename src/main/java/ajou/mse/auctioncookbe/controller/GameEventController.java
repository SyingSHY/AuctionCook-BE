package ajou.mse.auctioncookbe.controller;

import ajou.mse.auctioncookbe.entity.IncomingEvent;
import ajou.mse.auctioncookbe.service.GameEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GameEventController {

    @Autowired
    private GameEventService gameEventService;

    @GetMapping("/games/{gameID}/events")
    public List<IncomingEvent> fetchEvent(@PathVariable String gameID, @RequestParam String userID) {
        // 이벤트 버스에 저장된 이벤트 확인 후 수거

        List<IncomingEvent> resultEvent = gameEventService.fetchEvent(gameID, userID);

        return resultEvent;
    }

    @PostMapping("/games/{gameID}/events")
    public String receiveEvent(@PathVariable String gameID, @RequestParam String userID, @RequestBody IncomingEvent event) {

        gameEventService.postEventByPlayer(gameID, userID, event);

        return null;
    }
}
