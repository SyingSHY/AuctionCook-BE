package ajou.mse.auctioncookbe.entity.state;

import ajou.mse.auctioncookbe.entity.InGameRoom;
import ajou.mse.auctioncookbe.entity.Player;

public class GameStateReady implements IGameState {

    @Override
    public String toggleBidOrCook(InGameRoom gameRoom, String playerID) {
        Player player = gameRoom.getGamePlayer(playerID);
        return player.toggleGoingOnBid() ? "You will bid" : "You will cook";
    }

    @Override
    public String postBid(InGameRoom gameRoom, String playerID, int currentBid, int newBid) {
        return "Not Allowed in READY phase";
    }

    @Override
    public String postRecipe(InGameRoom gameRoom, String playerID, int recipeID) {
        return "Not Allowed in READY phase";
    }

    @Override
    public void nextState(InGameRoom gameRoom) {

    }
}
