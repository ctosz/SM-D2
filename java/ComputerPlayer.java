import ch.aplu.jcardgame.Card;
import ch.aplu.jgamegrid.Location;

/**
 * A computer player that implements a strategy to pick their cards to play.
 * @Author Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */

public class ComputerPlayer extends Player {
    private final ComputerStrategy strategy;

    public ComputerPlayer(Location handLocation, int playerNum, ComputerStrategy strategy) {
        super(handLocation, playerNum);
        this.strategy = strategy;
    }

    @Override
    public void takeTurn() {
        CountingUpGame.getInstance().setStatusText("Player " + playerNum + " thinking...");
        Card cardToPlay = strategy.pickCard(hand, gameManager);

        if (cardToPlay == null) {
            CountingUpGame.getInstance().setStatusText("Player " + playerNum + " skipping...");
            CountingUpGame.getInstance().thinkDelay();
        }

        gameManager.playCard(cardToPlay);
    }

    public ComputerStrategy getStrategy() {
        return strategy;
    }
}
