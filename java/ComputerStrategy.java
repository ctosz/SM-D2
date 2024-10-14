import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
/**
 * An interface for computer player strategies.
 * @Author Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */

public interface ComputerStrategy {

    /**
     * Determines the card to play based on different computer strategies.
     * @param hand The current hand of the computer player.
     * @param manager The GameManager handling validity checks and moves.
     * @return The selected card to play or null to skip the turn.
     */
    Card pickCard(Hand hand, GameManager manager);
}
