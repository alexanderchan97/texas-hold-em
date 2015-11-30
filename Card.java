/**
 * {@code Card} is a type containing a rank and a suit.
 *
 */
public class Card {

    private Rank rank;
    private Suit suit;
    
    public Card(Rank r, Suit s) {
        rank = r;
        suit = s;
    }
    
    public String toString() {
        return (rank.toString() + suit.toString());
    }
    
}
