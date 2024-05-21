package ajou.mse.auctioncookbe.controller;

import ajou.mse.auctioncookbe.DTO.InGameRoomDTO;
import ajou.mse.auctioncookbe.service.DummyService;
import ajou.mse.auctioncookbe.service.GameRoomManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GameRoomController {

    @Autowired
    private GameRoomManageService gameRoomManageService;
    @Autowired
    private DummyService dummyService;

    @GetMapping("/games/{gameID}")
    public InGameRoomDTO fetchGameStatus(@PathVariable String gameID) {
        return new InGameRoomDTO(gameRoomManageService.queryGameRoom(gameID));
    }

    @PutMapping("/games/{gameID}/ready")
    public String setReady(@PathVariable String gameID, @RequestParam String playerID) {
        return gameRoomManageService.setReady(gameID, playerID);
    }

    @PutMapping("/games/{gameID}/bid-cook")
    public String toggleBidOrCook(@PathVariable String gameID, @RequestParam String playerID) {
        return gameRoomManageService.toggleBorC(gameID, playerID);
    }

    @PostMapping("/games/{gameID}/bid")
    public String postBid(@PathVariable String gameID, @RequestParam String playerID, @RequestParam int currentBid, @RequestParam int newBid) {
        return gameRoomManageService.postBid(gameID, playerID, currentBid, newBid);
    }

    @PostMapping("/games/{gameID}/recipe")
    public String postRecipe(@PathVariable String gameID, @RequestParam String playerID, @RequestParam int recipeID) {
        return gameRoomManageService.postRecipe(gameID, playerID, recipeID);
    }

    @PutMapping("/games/{gameID}/dummy/ready")
    public String setReadyForDummy(@PathVariable String gameID, @RequestParam String playerID) {
        return dummyService.setDummyToGameReady(gameID, playerID);
    }
}
