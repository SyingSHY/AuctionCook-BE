package ajou.mse.auctioncookbe.entity.state;

import ajou.mse.auctioncookbe.entity.InGameRoom;

public class GameStateWait implements IGameState {

    @Override
    public String toggleBidOrCook(InGameRoom gameRoom, String playerID) {
        return "";
    }

    @Override
    public String postBid(InGameRoom gameRoom, String playerID, int currentBid, int newBid) {
        return "";
    }

    @Override
    public String postRecipe(InGameRoom gameRoom, String playerID, int recipeID) {
        return "";
    }

    @Override
    public void nextState(InGameRoom gameRoom) {

    }
}
