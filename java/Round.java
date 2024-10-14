import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
/**
 * A class that implements a round of the game, i.e. player turns before encountering
 * number of players - 1 skips.
 * @Author Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */
public class Round implements PlaySubscriber {
    private final Hand pile = new Hand(CountingUpGame.deck);
    private final int nbPlayers;
    private final GameManager gameManager;
    private int turnsSkipped = 0;

    public Round(int nbPlayers, GameManager gameManager) {
        this.nbPlayers = nbPlayers;
        this.gameManager = gameManager;
        gameManager.addSubscriber(this);
    }

    @Override
    public void cardPlayed(Card card) {
        if (card != null) {
            turnsSkipped = 0;
            card.setVerso(false);
            card.transfer(pile, true);
        } else {
            ++turnsSkipped;
        }
    }

    public int getRoundScore() {
        int score = 0;

        for (Card card : pile.getCardList()) {
            score += ((Rank) card.getRank()).getScoreCardValue();
        }

        return score;
    }

    public Hand getPile() {
        return pile;
    }

    public boolean isOver() {
        return turnsSkipped >= nbPlayers - 1;
    }
}
