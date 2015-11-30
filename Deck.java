import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Deck {

    private List<Card> d = new LinkedList<Card>();
    
    
    /**
     * Instantiates a new deck containing 52 unique cards.
     */
    public Deck() {
        for (Suit s : Suit.values()) {
            for (Rank r : Rank.values()) {
                d.add(new Card(r, s));
            }
        }
    }
    
    /**
     * Randomize the order of the deck.
     */
    public void shuffle() {
        Collections.shuffle(d);
    }
    
    /**
     * Deal the first card in the deck and remove it
     * from the deck.
     *
     * @return the first card in the deck
     */
    public Card deal() {
        Card c = d.get(0);
        d.remove(0);
        return c;
    }
    
    /**
     * Burn the first card in the deck.
     */
    public void burn() {
        d.remove(0);
    }
}
