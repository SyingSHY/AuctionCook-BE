package ajou.mse.auctioncookbe.entity.state;

import ajou.mse.auctioncookbe.entity.InGameRoom;

public interface IGameState {

     String toggleBidOrCook(InGameRoom gameRoom, String playerID);
     String postBid(InGameRoom gameRoom, String playerID, int currentBid, int newBid);
     String postRecipe(InGameRoom gameRoom, String playerID, int recipeID);
     void nextState(InGameRoom gameRoom);
}
