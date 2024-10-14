import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.Location;
/**
 * An abstract class for the combined concept of a human and computer player.
 * @Author Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */

public abstract class Player {
    private int score = 0;
    protected Hand hand = new Hand(CountingUpGame.deck);
    private final Location handLocation;
    protected final GameManager gameManager = CountingUpGame.getInstance().getGameManager();
    protected final int playerNum;

    public Player(Location handLocation, int playerNum) {
        this.handLocation = handLocation;
        this.playerNum = playerNum;
    }

    public abstract void takeTurn();

    public void addToHand(Card card) {
        hand.insert(card, false);
        sortHand();
    }

    public void sortHand() {
        hand.sort(Hand.SortType.SUITPRIORITY, false);
    }

    public int getScore() {
        return score;
    }

    public Location getHandLocation() {
        return handLocation;
    }

    public Hand getHand() {
        return hand;
    }

    public void addScore(int score) {
        this.score += score;
    }

    private int handScore() {
        int score = 0;

        for (Card card : hand.getCardList()) {
            score += ((Rank) card.getRank()).getScoreCardValue();
        }

        return score;
    }

    public void endGame() {
        score -= handScore();
    }
}
