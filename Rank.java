/**
 * {@code Rank} is an enumerated type that lists
 * all possible ranks of a card. Each value has
 * a shortened String representation as well as
 * an int representation.
 *
 */
public enum Rank {
    
    Ace("A", 1),
    Two("2", 2),
    Three("3", 3),
    Four("4", 4),
    Five("5", 5),
    Six("6", 6),
    Seven("7", 7),
    Eight("8", 8),
    Nine("9", 9),
    Ten("10", 10),
    Jack("J", 11),
    Queen("Q", 12),
    King("K", 13);
    
    private final String strVal;
    private final int intVal;
    
    Rank(String s, int i) {
        strVal = s;
        intVal = i;
    }
    
    public String getString() {
        return strVal;
    }
    
    public int getInt() {
        return intVal;
    }

}
