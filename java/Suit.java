/**
 * An enum for the suits of the cards.
 */

public enum Suit {
    SPADES ("S"), HEARTS ("H"), DIAMONDS ("D"), CLUBS ("C");
    private String suitShortHand = "";
    Suit(String shortHand) {
        this.suitShortHand = shortHand;
    }

    public String getSuitShortHand() {
        return suitShortHand;
    }
}
