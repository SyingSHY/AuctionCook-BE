package ajou.mse.auctioncookbe.entity.state;

import ajou.mse.auctioncookbe.entity.InGameRoom;
import ajou.mse.auctioncookbe.entity.Player;

public class GameStateStart implements IGameState {

    private InGameRoom assignedGameRoom;

    public GameStateStart(InGameRoom gameRoom) {
        this.assignedGameRoom = gameRoom;
    }

    @Override
    public String toggleBidOrCook(String playerID) {
        return "Not Allowed in START phase";
    }

    @Override
    public String postBid(String playerID, int currentBid, int newBid) {
        Player player = assignedGameRoom.getGamePlayer(playerID);

        if (player.isGoingOnBid()) {
            return player.putBidding(currentBid, newBid) + "";
        }
        else {
            return "You CANNOT BID when you're cooking";
        }
    }

    @Override
    public String postRecipe(String playerID, int recipeID) {
        Player player = assignedGameRoom.getGamePlayer(playerID);

        if (player.isGoingOnBid()) {
            return player.cookRecipe(recipeID).toString();
        }
        else {
            return "You CANNOT COOK when you're bidding";
        }
    }

    @Override
    public void moveNextState(IGameState gameState) {
        if (gameState == null) {
            assignedGameRoom.moveNextGameState(assignedGameRoom.getGameEndState());
        }
        else {
            assignedGameRoom.moveNextGameState(gameState);
        }
    }

    @Override
    public String toString() {
        return "START";
    }
}
