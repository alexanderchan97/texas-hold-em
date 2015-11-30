import java.util.Collection;
import java.util.TreeSet;

public abstract class Player {

    private int chips;
    private Collection<Card> hole = new TreeSet<Card>();
    private int id;
    
    public Player(int id, int chips) {
        this.id = id;
        this.chips = chips;
    }
    
    public int getChips() {
        return chips;
    }
    
    public int getId() {
        return id;
    }
    
    public void addCard(Card c) {
        if (hole.size() == 2) {
            throw new IllegalStateException();
        }
        hole.add(c);
    }
    
    public void removeHoleCards() {
        hole.clear();
    }
    
    public abstract Action getAction();
    
}
