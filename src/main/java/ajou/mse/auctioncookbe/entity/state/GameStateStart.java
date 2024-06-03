package ajou.mse.auctioncookbe.entity.state;

import ajou.mse.auctioncookbe.entity.InGameRoom;
import ajou.mse.auctioncookbe.entity.Player;

public class GameStateStart implements IGameState {

    @Override
    public String toggleBidOrCook(InGameRoom gameRoom, String playerID) {
        return "Not Allowed in START phase";
    }

    @Override
    public String postBid(InGameRoom gameRoom, String playerID, int currentBid, int newBid) {
        Player player = gameRoom.getGamePlayer(playerID);

        if (player.isGoingOnBid()) {
            return player.putBidding(currentBid, newBid) + "";
        }
        else {
            return "You CANNOT BID when you're cooking";
        }
    }

    @Override
    public String postRecipe(InGameRoom gameRoom, String playerID, int recipeID) {
        Player player = gameRoom.getGamePlayer(playerID);

        if (player.isGoingOnBid()) {
            return player.cookRecipe(recipeID).toString();
        }
        else {
            return "You CANNOT COOK when you're bidding";
        }
    }

    @Override
    public void nextState(InGameRoom gameRoom) {

    }
}
