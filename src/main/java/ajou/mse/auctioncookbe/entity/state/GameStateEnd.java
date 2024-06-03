package ajou.mse.auctioncookbe.entity.state;

import ajou.mse.auctioncookbe.entity.InGameRoom;

public class GameStateEnd implements IGameState {

    @Override
    public String toggleBidOrCook(InGameRoom gameRoom, String playerID) {
        return "Not Allowed in END phase";
    }

    @Override
    public String postBid(InGameRoom gameRoom, String playerID, int currentBid, int newBid) {
        return "Not Allowed in END phase";
    }

    @Override
    public String postRecipe(InGameRoom gameRoom, String playerID, int recipeID) {
        return "Not Allowed in END phase";
    }

    @Override
    public void nextState(InGameRoom gameRoom) {

    }
}
