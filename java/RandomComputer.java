import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.Random;
/**
 * A computer strategy that picks a random valid card to play from its hand.
 * @Author Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */

public class RandomComputer implements ComputerStrategy {
    private static final Random random = new Random();
    @Override
    public Card pickCard(Hand hand, GameManager gameManager) {
        ArrayList<Card> validCards = new ArrayList<>();

        for (Card card : hand.getCardList()) {
            if (gameManager.isValid(card)) {
                validCards.add(card);
            }
        }

        if (validCards.isEmpty()) {
            return null;
        }

        int cardIndex = random.nextInt(validCards.size());

        return validCards.get(cardIndex);
    }
}
