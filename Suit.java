/**
 * {@code Suit} is an enumerated type that lists the
 * four suits in a deck of cards. Each value has a
 * shortened String representation.
 *
 */
public enum Suit {
    SPADES("s"),
    HEARTS("h"),
    CLUBS("c"),
    DIAMONDS("d");
    
    private final String value;
    
    Suit(String value) {
        this.value = value;
    }
    
    public String getString() {
        return value;
    }
    
}
