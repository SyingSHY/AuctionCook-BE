package ajou.mse.auctioncookbe.entity.state;

import ajou.mse.auctioncookbe.entity.InGameRoom;

public class GameStateFinish implements IGameState {

    private InGameRoom assignedGameRoom;

    public GameStateFinish(InGameRoom gameRoom) {
        this.assignedGameRoom = gameRoom;
    }

    @Override
    public String toggleBidOrCook(String playerID) {
        return "";
    }

    @Override
    public String postBid(String playerID, int currentBid, int newBid) {
        return "";
    }

    @Override
    public String postRecipe(String playerID, int recipeID) {
        return "";
    }

    @Override
    public void moveNextState(IGameState gameState) {

    }

    @Override
    public String toString() {
        return "FINISH";
    }
}
