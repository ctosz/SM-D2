import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jcardgame.TargetArea;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

import java.awt.*;
import java.util.ArrayList;

/**
 * A class that manages the display (i.e. players' hands, scores) of the game.
 * some implementations were taken from the original CountingUpGame class.
 * @Author Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */

public class DisplayManager {
    private final static int handWidth = 400;
    private final static int trickWidth = 40;
    private final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(575, 25),
            new Location(575, 575)
    };
    private final Actor[] scoreActors = {null, null, null, null};
    private final Location trickLocation = new Location(350, 350);
    private final Location textLocation = new Location(350, 450);
    private final Location hideLocation = new Location(-500, -500);
    Font bigFont = new Font("Arial", Font.BOLD, 36);

    private final CountingUpGame game;
    private final ArrayList<Player> players;

    public DisplayManager(CountingUpGame game) {
        this.game = game;
        this.players = game.getPlayers();
    }

    public void initUI() {
        initScoreDisplay();

        RowLayout[] layouts = new RowLayout[players.size()];
        for (int i = 0; i < players.size(); i++) {
            layouts[i] = new RowLayout(players.get(i).getHandLocation(), handWidth);
            layouts[i].setRotationAngle(90 * i);

            Hand hand = players.get(i).getHand();
            hand.setView(game, layouts[i]);
            hand.setTargetArea(new TargetArea(trickLocation));
            hand.draw();
        }
    }

    private void initScoreDisplay() {
        for (int i = 0; i < players.size(); ++i) {
            updateScoreDisplay(i);
        }
    }

    public void updateScoreDisplay(int player) {
        if (scoreActors[player] != null) {
            game.removeActor(scoreActors[player]);
        }

        int displayScore = players.get(player).getScore();
        String text = "P" + player + "[" + displayScore + "]";
        scoreActors[player] = new TextActor(text, Color.WHITE, game.bgColor, bigFont);
        game.addActor(scoreActors[player], scoreLocations[player]);
    }

    public void setStatus(String string) {
        game.setStatusText(string);
    }

    public void updatePile(Hand pile) {
        pile.setView(game, new RowLayout(trickLocation, (pile.getNumberOfCards() + 2) * trickWidth));
        pile.draw();
    }

    public void resetPile(Hand pile) {
        pile.setView(game, new RowLayout(hideLocation, 0));
        pile.draw();
    }

    public void gameOver(String winText) {
        setStatus(winText);
        game.addActor(new Actor("sprites/gameover.gif"), textLocation);
        game.refresh();
    }
}
