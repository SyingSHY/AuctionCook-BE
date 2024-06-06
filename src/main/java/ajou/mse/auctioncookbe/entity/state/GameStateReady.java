package ajou.mse.auctioncookbe.entity.state;

import ajou.mse.auctioncookbe.entity.InGameRoom;
import ajou.mse.auctioncookbe.entity.Player;

public class GameStateReady implements IGameState {

    private InGameRoom assignedGameRoom;

    public GameStateReady(InGameRoom gameRoom) {
        this.assignedGameRoom = gameRoom;
    }

    @Override
    public String toggleBidOrCook(String playerID) {
        Player player = assignedGameRoom.getGamePlayer(playerID);
        return player.toggleGoingOnBid() ? "You will bid" : "You will cook";
    }

    @Override
    public String postBid(String playerID, int currentBid, int newBid) {
        return "Not Allowed in READY phase";
    }

    @Override
    public String postRecipe(String playerID, int recipeID) {
        return "Not Allowed in READY phase";
    }

    @Override
    public void moveNextState(IGameState gameState) {
        if (gameState == null) {
            assignedGameRoom.moveNextGameState(assignedGameRoom.getGameStartState());
        }
        else {
            assignedGameRoom.moveNextGameState(gameState);
        }
    }

    @Override
    public String toString() {
        return "READY";
    }
}
