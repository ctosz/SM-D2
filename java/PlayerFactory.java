import ch.aplu.jgamegrid.Location;

/**
 * A concrete factory for player creation.
 * @Author Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */

public class PlayerFactory {
    private String type;
    private final GameManager gameManager = CountingUpGame.getInstance().getGameManager();

    public PlayerFactory(String type) {
        this.type = type;
    }

    public Player createPlayer(Location handLocation, int playerNum) {
        switch (type) {
            case "basic":
                return new ComputerPlayer(handLocation, playerNum, new BasicComputer());
            case "random":
                return new ComputerPlayer(handLocation, playerNum, new RandomComputer());
            case "clever":
                ComputerPlayer clever = new ComputerPlayer(handLocation, playerNum, new CleverComputer());
                gameManager.addSubscriber((PlaySubscriber) clever.getStrategy());
                return clever;
            case "human":
                return new HumanPlayer(handLocation, playerNum);
        }
        return null;
    }
}
