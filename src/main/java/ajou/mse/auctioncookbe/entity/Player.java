package ajou.mse.auctioncookbe.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private String playerID;
    private String playerName;
    private boolean finishedLoading;
    private boolean goingOnBid;
    private int token;
    private int score;
    private int bidding;
    private List<Integer> recipeList;

    public Player(User user) {
        this.playerID = user.getRedisId();
        this.playerName = user.getName();
        this.finishedLoading = false;
        this.goingOnBid = true;
        this.token = 30;
        this.score = 0;
        this.bidding = 0;
        this.recipeList = new ArrayList<>();
    }

    public void finishLoading() {
        this.finishedLoading = true;
    }

    public boolean toggleGoingOnBid() {
        this.goingOnBid = !this.goingOnBid;
        return this.goingOnBid;
    }

    public List<Integer> cookRecipe(int recipe) {
        this.recipeList.add(recipe);
        return this.recipeList;
    }

    public boolean putBidding(int currentBid, int newBid) {
        if (this.bidding == currentBid) {
            this.bidding = newBid;
            return true;
        }
        else return false;
    }

    public void winBidding() {
        this.token -= this.bidding;
        this.bidding = 0;
    }

    public void loseBidding() {
        this.bidding = 0;
    }
}
