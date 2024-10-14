import ch.aplu.jcardgame.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * A class that handles game logs.
 * some implementations were taken from the original CountingUpGame class.
 * @Author Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */

public class Logger implements PlaySubscriber {
    private StringBuilder logResult = new StringBuilder();
    private int playerNum = 0;
    private final ArrayList<Player> players;
    private final int nbPlayers;

    public Logger(GameManager gameManager, ArrayList<Player> players, int nbPlayers) {
        gameManager.addSubscriber(this);
        this.players = players;
        this.nbPlayers = nbPlayers;
    }

    @Override
    public void cardPlayed(Card card) {
        if (card == null) {
            logResult.append("P" + playerNum + "-SKIP,");
        } else {
            Rank cardRank = (Rank) card.getRank();
            Suit cardSuit = (Suit) card.getSuit();
            logResult.append("P" + playerNum + "-" + cardRank.getRankCardLog() + cardSuit.getSuitShortHand() + ",");
        }

        playerNum = (playerNum + 1) % nbPlayers;
    }

    public void addRoundInfoToLog(int roundNumber) {
        logResult.append("Round" + roundNumber + ":");
    }

    public void addEndOfRoundToLog() {
        logResult.append("Score:");
        for (Player player : players) {
            logResult.append(player.getScore() + ",");
        }
        logResult.append("\n");
    }

    public void addEndOfGameToLog(List<Integer> winners) {
        logResult.append("EndGame:");
        for (Player player : players) {
            logResult.append(player.getScore() + ",");
        }
        logResult.append("\n");
        logResult.append("Winners:" + String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList())));
    }

    public String getLogResult() {
        return logResult.toString();
    }
}
