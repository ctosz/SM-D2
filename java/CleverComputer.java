import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import java.util.ArrayList;

/**
 * A clever computer strategy that picks the lowest ranked card that is valid to play,
 * but only if it can beat the strongest response to that card.
 * @Author Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */



public class CleverComputer implements ComputerStrategy, PlaySubscriber {

    private final ArrayList<Card> cardsNotPlayed = CountingUpGame.deck.toHand().getCardList();

    @Override
    public Card pickCard(Hand hand, GameManager gameManager) {
        ArrayList<Card> validCards = new ArrayList<>();

        for (Card card : hand.getCardList()) {
            if (gameManager.isValid(card) && canBeatStrongestResponse(card, gameManager, hand)) {
                validCards.add(card);
            }
        }

        if (validCards.size() == 0) {
            // If no cards can beat the strongest response, skip the turn
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

    @Override
    public void cardPlayed(Card card) {
        if (card != null) {
            // Remove card from list of cards that haven't been played yet
            cardsNotPlayed.remove(card);
        }
    }

    private boolean canBeatStrongestResponse(Card card, GameManager gameManager, Hand hand) {
        for (Card response : strongestResponses(card, gameManager, hand)) {
            boolean beatable = false;

            for (Card possibleResponse : hand.getCardList()) {
                if (possibleResponse.equals(card)) {
                    continue;
                }

                if (gameManager.isValid(possibleResponse, response)) {
                    beatable = true;
                    break;
                }
            }

            if (!beatable) {
                return false;
            }
        }

        return true;
    }

    private ArrayList<Card> strongestResponses(Card card, GameManager gameManager, Hand hand) {
        ArrayList<Card> validResponses = new ArrayList<>();

        for (Card response : cardsNotPlayed) {
            // Identify valid response cards from the cards that haven't been played yet.
            if (gameManager.isValid(response, card) && !hand.contains(response)) {
                validResponses.add(response);
            }
        }

        // Find the highest-ranking valid response cards.
        ArrayList<Card> strongestResponses = new ArrayList<>();
        for (Card response : validResponses) {
            if (strongestResponses.size() == 0 || gameManager.rankEqual(response, strongestResponses.get(0))) {
                strongestResponses.add(response);
            } else if (gameManager.rankGreater(response, strongestResponses.get(0))
                        && gameManager.suitEqual(response, strongestResponses.get(0))) {
                strongestResponses.clear();
                strongestResponses.add(response);
            }
        }

        return strongestResponses;
    }
}
