package ajou.mse.auctioncookbe.entity.state;

import ajou.mse.auctioncookbe.entity.InGameRoom;

public class GameStateEnd implements IGameState {

    private InGameRoom assignedGameRoom;

    public GameStateEnd(InGameRoom gameRoom) {
        this.assignedGameRoom = gameRoom;
    }

    @Override
    public String toggleBidOrCook(String playerID) {
        return "Not Allowed in END phase";
    }

    @Override
    public String postBid(String playerID, int currentBid, int newBid) {
        return "Not Allowed in END phase";
    }

    @Override
    public String postRecipe(String playerID, int recipeID) {
        return "Not Allowed in END phase";
    }

    @Override
    public void moveNextState(IGameState gameState) {
        if (gameState == null) {
            assignedGameRoom.moveNextGameState(assignedGameRoom.getGameReadyState());
        }
        else {
            assignedGameRoom.moveNextGameState(gameState);
        }
    }

    @Override
    public String toString() {
        return "END";
    }
}
