import ch.aplu.jcardgame.Card;

/**
 * An interface for subscribers to events of the game namely what Card was played.
 * @Author Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */

public interface PlaySubscriber {

    void cardPlayed(Card card);
}
