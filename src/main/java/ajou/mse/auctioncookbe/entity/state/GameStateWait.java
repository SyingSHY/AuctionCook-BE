package ajou.mse.auctioncookbe.entity.state;

import ajou.mse.auctioncookbe.entity.InGameRoom;

public class GameStateWait implements IGameState {

    private InGameRoom assignedGameRoom;

    public GameStateWait(InGameRoom gameRoom) {
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
        if (gameState == null) {
            assignedGameRoom.moveNextGameState(assignedGameRoom.getGameReadyState());
        }
        else {
            assignedGameRoom.moveNextGameState(gameState);
        }
    }
}
