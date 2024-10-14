import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import java.util.ArrayList;

/**
 * A basic computer strategy that picks the lowest ranked card that is valid to play.
 * @author Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */

public class BasicComputer implements ComputerStrategy {
    @Override
    public Card pickCard(Hand hand, GameManager gameManager) {
        ArrayList<Card> validCards = new ArrayList<>();

        for (Card card : hand.getCardList()) {
            if (gameManager.isValid(card)) {
                validCards.add(card);
            }
        }

        if (validCards.size() == 0) {
            return null;
        }

        Card lowestCard = validCards.get(0);

        for (int i = 1; i < validCards.size(); ++i) {
            if (gameManager.rankGreater(lowestCard, validCards.get(i))) {
                lowestCard = validCards.get(i);
            }
        }

        return lowestCard;
    }
}
