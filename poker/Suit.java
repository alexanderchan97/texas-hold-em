package poker;
/**
 * {@code Suit} is an enumerated type that lists the
 * four suits in a deck of cards. Each value has a
 * shortened String representation.
 *
 */
public enum Suit {
    // 64
    
    SPADES    ("s", 2),
    HEARTS    ("h", 1),
    CLUBS     ("c", 3),
    DIAMONDS  ("d", 0);
    
    private final String s;
    private final int y;
    public static final int HEIGHT = 64;
    public static final int GAP = 9;
    
    Suit(String s, int i) {
        this.s = s;
        y = i * (HEIGHT + GAP);
    }
    
    @Override
    public String toString() {
        return s;
    }
    
    public int getY() {
        return y;
    }
    
    public static Suit fromString(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        for (Suit st : Suit.values()) {
            if (s.equals(st.toString())) {
                return st;
            }
        }
        throw new IllegalArgumentException();
    }

    
}
