package ajou.mse.auctioncookbe.entity.state;

public interface IGameState {

     String toggleBidOrCook(String playerID);
     String postBid(String playerID, int currentBid, int newBid);
     String postRecipe(String playerID, int recipeID);
     void moveNextState(IGameState gameState);
}
