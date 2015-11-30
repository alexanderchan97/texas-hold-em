/**
 * {@code Card} is a type containing a rank and a suit.
 *
 */
public class Card {

    private Rank rank;
    private Suit suit;
    
    /**
     * Instantiates a new card.
     *
     * @param r the rank of the card
     * @param s the suit of the card
     */
    public Card(Rank r, Suit s) {
        rank = r;
        suit = s;
    }
    
    @Override
    public String toString() {
        return (rank.toString() + suit.toString());
    }
    
}
