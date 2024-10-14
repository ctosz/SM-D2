import ch.aplu.jcardgame.Card;
import java.util.ArrayList;

/**
 * A class that manages the game's detailed flow of logic, i.e. validity checking.
 * @Author Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */

public class GameManager {
    private final ArrayList<PlaySubscriber> subscribers = new ArrayList<>();
    private Card lastPlayedCard = null;

    public void addSubscriber(PlaySubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(PlaySubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public boolean isValid(Card card) {
        // pass is observed as a valid null Card
        if (lastPlayedCard == null || card == null)
            return true;

        return (rankGreater(card, lastPlayedCard) && suitEqual(card, lastPlayedCard))
                || (rankEqual(card, lastPlayedCard));
    }

    // can card1 be played on card2
    public boolean isValid(Card card1, Card card2) {
        if (card2 == null || card1 == null)
            return true;

        return (rankGreater(card1, card2) && suitEqual(card1, card2))
                || (rankEqual(card1, card2));
    }

    public void playCard(Card card) {
        if (isValid(card)) {
            if (card != null)
                lastPlayedCard = card;
            notifySubscribers(card);
        }
    }

    public void endRound(Round round) {
        removeSubscriber(round);
        lastPlayedCard = null;
    }

    private void notifySubscribers(Card card) {
        for (PlaySubscriber subscriber : subscribers) {
            subscriber.cardPlayed(card);
        }
    }

    public boolean rankGreater(Card card1, Card card2) {
        Rank card1Rank = (Rank) card1.getRank();
        Rank card2Rank = (Rank) card2.getRank();

        return card1Rank.getRankCardValue() > card2Rank.getRankCardValue();
    }

    public boolean suitEqual(Card card1, Card card2) {
        return card1.getSuit() == card2.getSuit();
    }

    public boolean rankEqual(Card card1, Card card2) {
        return card1.getRank() == card2.getRank();
    }

}
