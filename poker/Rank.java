package poker;
/**
 * {@code Rank} is an enumerated type that lists
 * all possible ranks of a card. Each value has
 * a shortened String representation as well as
 * an int representation.
 *
 */
public enum Rank {
    
    DEUCE  ("2", 2),
    THREE  ("3", 3),
    FOUR   ("4", 4),
    FIVE   ("5", 5),
    SIX    ("6", 6),
    SEVEN  ("7", 7),
    EIGHT  ("8", 8),
    NINE   ("9", 9),
    TEN    ("T", 10),
    JACK   ("J", 11),
    QUEEN  ("Q", 12),
    KING   ("K", 13),
    ACE    ("A", 14);
    
    private final String strVal;
    private final int intVal;
    private final int x;
    public static final int WIDTH = 43;
    public static final int GAP = 9;
    
    Rank(String s, int i) {
        strVal = s;
        intVal = i;
        x = ((intVal - 1) % 13) * (WIDTH + GAP);
    }
    
    @Override
    public String toString() {
        return strVal;
    }
    
    public int toInt() {
        return intVal;
    }
    
    public int getX() {
        return x;
    }
    
    public static Rank fromString(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        for (Rank r : Rank.values()) {
            if (s.equals(r.toString())) {
                return r;
            }
        }
        throw new IllegalArgumentException();
    }

}
