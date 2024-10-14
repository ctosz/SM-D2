import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Modified and extended version of the card game COUNTING UP by ABC Games.
 * A singleton class that manages the game's initialisation and the core logic flow.
 * @Author ABC Games
 * implemented by Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */

public class CountingUpGame extends CardGame {
    private static CountingUpGame instance;

    static public final int seed = 30008;
    static final Random random = new Random(seed);
    private final Properties properties;
    private final List<List<String>> playerAutoMovements = new ArrayList<>();

    private final static String version = "1.0";
    public final int nbPlayers = 4;
    public ArrayList<Player> players = new ArrayList<>();
    public final static Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
    private final Location[] handLocations = {
            new Location(350, 625),
            new Location(75, 350),
            new Location(350, 75),
            new Location(625, 350)
    };
    private final int thinkingTime;
    private final int delayTime;

    private final int[] autoIndexHands = new int [nbPlayers];
    private final boolean isAuto;

    private final DisplayManager displayManager = new DisplayManager(this);
    private final GameManager gameManager = new GameManager();
    private final Logger logger = new Logger(gameManager, players, nbPlayers);

    private void initGame() {
        setUpPlayers();
        dealingOut();
    }

    private void setUpPlayers() {
        for (int i = 0; i < nbPlayers; i++) {
            // fetching player type from properties file
            String playerType = properties.getProperty("players." + i);
            PlayerFactory pf = new PlayerFactory(playerType);
            Player player = pf.createPlayer(handLocations[i], i);
            players.add(player);
        }
    }

    // return random Card from ArrayList
    public static Card randomCard(ArrayList<Card> list) {
        int x = random.nextInt(list.size());
        return list.get(x);
    }

    private Rank getRankFromString(String cardName) {
        String rankString = cardName.substring(0, cardName.length() - 1);
        int rankValue = Integer.parseInt(rankString);

        for (Rank rank : Rank.values()) {
            if (rank.getRankCardValue() == rankValue) {
                return rank;
            }
        }

        return Rank.ACE;
    }

    private Suit getSuitFromString(String cardName) {
        String suitString = cardName.substring(cardName.length() - 1);

        for (Suit suit : Suit.values()) {
            if (suit.getSuitShortHand().equals(suitString)) {
                return suit;
            }
        }
        return Suit.CLUBS;
    }

    private Card getCardFromList(List<Card> cards, String cardName) {
        Rank cardRank = getRankFromString(cardName);
        Suit cardSuit = getSuitFromString(cardName);
        for (Card card: cards) {
            if (card.getSuit() == cardSuit
                    && card.getRank() == cardRank) {
                return card;
            }
        }

        return null;
    }

    private void dealingOut() {
        Hand pack = deck.toHand(false);
        int nbCardsPerPlayer = pack.getNumberOfCards() / nbPlayers;

        for (int i = 0; i < nbPlayers; i++) {
            String initialCardsKey = "players." + i + ".initialcards";
            String initialCardsValue = properties.getProperty(initialCardsKey);

            if (initialCardsValue == null) {
                continue;
            }

            String[] initialCards = initialCardsValue.split(",");

            for (String initialCard : initialCards) {
                if (initialCard.length() <= 1) {
                    continue;
                }

                Card card = getCardFromList(pack.getCardList(), initialCard);

                if (card != null) {
                    card.removeFromHand(false);
                    players.get(i).addToHand(card);
                }
            }
        }

        for (int i = 0; i < nbPlayers; i++) {
            int cardsToDeal = nbCardsPerPlayer - players.get(i).getHand().getNumberOfCards();
            for (int j = 0; j < cardsToDeal; j++) {
                if (pack.isEmpty()) return;
                Card dealt = randomCard(pack.getCardList());
                dealt.removeFromHand(false);
                players.get(i).addToHand(dealt);
            }
        }
    }

    private int playerIndexWithAceClub() {
        for (int i = 0; i < nbPlayers; i++) {
            Hand hand = players.get(i).getHand();
            List<Card> cards = hand.getCardsWithRank(Rank.ACE);

            if (cards.size() == 0) {
                continue;
            }

            for (Card card : cards) {
                if (card.getSuit() == Suit.CLUBS) {
                    return i;
                }
            }
        }

        return 0;
    }

    private void playGame() {
        // End trump suit
        Round round = new Round(nbPlayers, gameManager);
        int winner;
        int roundNumber = 1;
        boolean isContinue = true;
        boolean firstTurn = true;
        logger.addRoundInfoToLog(roundNumber);

        int nextPlayer = playerIndexWithAceClub();

        while(isContinue) {
            boolean finishedAuto = false;

            if (isAuto) {
                firstTurn = false;
                int nextPlayerAutoIndex = autoIndexHands[nextPlayer];
                List<String> nextPlayerMovement = playerAutoMovements.get(nextPlayer);
                String nextMovement;

                if (nextPlayerMovement.size() > nextPlayerAutoIndex) {
                    nextMovement = nextPlayerMovement.get(nextPlayerAutoIndex);
                    nextPlayerAutoIndex++;

                    autoIndexHands[nextPlayer] = nextPlayerAutoIndex;
                    Hand nextHand = players.get(nextPlayer).getHand();

                    if (nextMovement.equals("SKIP")) {
                        setStatusText("Player " + nextPlayer + " skipping...");
                        delay(thinkingTime);
                        gameManager.playCard(null);
                    } else {
                        setStatusText("Player " + nextPlayer + " thinking...");
                        delay(thinkingTime);
                        gameManager.playCard(getCardFromList(nextHand.getCardList(), nextMovement));
                    }
                } else {
                    finishedAuto = true;
                }
            }

            if (!isAuto || finishedAuto) {

                if (firstTurn) {
                    firstTurn = false;
                    // Start game by playing Ace of Club
                    gameManager.playCard(players.get(nextPlayer).getHand().getCard(Suit.CLUBS, Rank.ACE));
                    nextPlayer = (nextPlayer + 1) % nbPlayers;
                }
                // Follow with selected card
                players.get(nextPlayer).takeTurn();
            }

            displayManager.updatePile(round.getPile());

            if (round.isOver()) {
                displayManager.resetPile(round.getPile());
                winner = (nextPlayer + 1) % nbPlayers;
                players.get(winner).addScore(round.getRoundScore());
                displayManager.updateScoreDisplay(winner);
                logger.addEndOfRoundToLog();
                roundNumber++;
                logger.addRoundInfoToLog(roundNumber);
                delay(delayTime);
                gameManager.endRound(round);
                round = new Round(nbPlayers, gameManager);
            }

            for (Player player : players) {
                if (player.getHand().getNumberOfCards() == 0) {
                    isContinue = false;
                    break;
                }
            }

            if (!isContinue) {
                winner = nextPlayer;
                players.get(winner).addScore(round.getRoundScore());
                logger.addEndOfRoundToLog();
            } else {
                nextPlayer = (nextPlayer + 1) % nbPlayers;
            }

            delay(delayTime);
        }

        for (Player player : players) {
            player.endGame();
        }
    }


    private void setupPlayerAutoMovements() {
        String player0AutoMovement = properties.getProperty("players.0.cardsPlayed");
        String player1AutoMovement = properties.getProperty("players.1.cardsPlayed");
        String player2AutoMovement = properties.getProperty("players.2.cardsPlayed");
        String player3AutoMovement = properties.getProperty("players.3.cardsPlayed");

        String[] playerMovements = new String[] {"", "", "", ""};
        if (player0AutoMovement != null) {
            playerMovements[0] = player0AutoMovement;
        }

        if (player1AutoMovement != null) {
            playerMovements[1] = player1AutoMovement;
        }

        if (player2AutoMovement != null) {
            playerMovements[2] = player2AutoMovement;
        }

        if (player3AutoMovement != null) {
            playerMovements[3] = player3AutoMovement;
        }

        for (String movementString : playerMovements) {
            List<String> movements = Arrays.asList(movementString.split(","));
            playerAutoMovements.add(movements);
        }
    }

    public String runApp() {
        setTitle("CountingUpGame (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
        setupPlayerAutoMovements();
        initGame();
        displayManager.initUI();

        playGame();

        for (int i = 0; i < nbPlayers; i++)
            displayManager.updateScoreDisplay(i);

        int maxScore = 0;
        for (Player player : players)
            if (player.getScore() > maxScore)
                maxScore = player.getScore();

        List<Integer> winners = new ArrayList<>();
        for (int i = 0; i < nbPlayers; i++)
            if (players.get(i).getScore() == maxScore)
                winners.add(i);

        String winText;
        if (winners.size() == 1) {
            winText = "Game over. Winner is player: " +
                    winners.iterator().next();
        } else {
            winText = "Game Over. Drawn winners are players: " +
                    String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList()));
        }

        displayManager.gameOver(winText);
        logger.addEndOfGameToLog(winners);

        return logger.getLogResult();
    }

    public CountingUpGame(Properties properties) {
        super(700, 700, 30);
        this.properties = properties;
        isAuto = Boolean.parseBoolean(properties.getProperty("isAuto"));
        thinkingTime = Integer.parseInt(properties.getProperty("thinkingTime", "200"));
        delayTime = Integer.parseInt(properties.getProperty("delayTime", "100"));
        instance = this;
    }

    public static CountingUpGame getInstance() {
        return instance;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void delay() {
        delay(delayTime);
    }

    public void thinkDelay() {
        delay(thinkingTime);
    }

    public ArrayList<Player> getPlayers() { return this.players; }
}
