import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author achan
 *
 */
public class Deck {

    private List<Card> d = new LinkedList<Card>();
    
    public Deck() {
        for (Suit s : Suit.values()) {
            for (Rank r : Rank.values()) {
                d.add(new Card(r, s));
            }
        }
    }
    
    public void shuffle() {
        Collections.shuffle(d);
    }
    
    public Card deal() {
        Card c = d.get(0);
        d.remove(0);
        return c;
    }
}
