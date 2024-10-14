import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jgamegrid.GGKeyListener;
import ch.aplu.jgamegrid.Location;
import java.awt.event.KeyEvent;

/**
 * A human player that waits for user input i.e. (mouse click) to play their cards.
 * @Author Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */


public class HumanPlayer extends Player implements GGKeyListener {
    private Card selected;
    private boolean isWaitingForPass = false;
    private boolean passSelected = false;

    public HumanPlayer(Location handLocation, int playerNum) {
        super(handLocation, playerNum);
        CardListener cardListener = new CardAdapter()  // Human Player plays card
        {
            public void leftDoubleClicked(Card card) {
                selected = card;
                hand.setTouchEnabled(false);
            }
        };
        hand.addCardListener(cardListener);
        CountingUpGame.getInstance().addKeyListener(this);
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (isWaitingForPass && keyEvent.getKeyChar() == '\n') {
            selected = null;
            passSelected = true;
        }
        return false;
    }

    @Override
    public boolean keyReleased(KeyEvent keyEvent) {
        return false;
    }

    @Override
    public void takeTurn() {
        CountingUpGame.getInstance().setStatusText("Player 0 double-click on card to follow or press Enter to pass");

        do {
            selected = null;
            hand.setTouchEnabled(true);
            isWaitingForPass = true;
            passSelected = false;

            while (selected == null && !passSelected)
                CountingUpGame.getInstance().delay();

            isWaitingForPass = false;
        } while (!gameManager.isValid(selected));

        gameManager.playCard(selected);
    }
}
