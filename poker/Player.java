package poker;
import java.io.Serializable;
import java.util.Collection;
import java.util.TreeSet;

// TODO: Auto-generated Javadoc
/**
 * The abstract class {@code Player} denotes either a human player or an AI bot.
 * Each player has an id, made distinct by the server. The player also has a chip
 * count and a set of cards, also handled by the server.
 */
public abstract class Player implements Serializable, Comparable<Player> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -716532766336699698L;
    private int chips;
    private Collection<Card> hole = new TreeSet<Card>();
    private int id;
    
    /**
     * Instantiates a new player.
     *
     * @param id player id
     * @param chips player's chip count
     */
    public Player(int id, int chips) {
        this.id = id;
        this.chips = chips;
    }
    
    /**
     * Gets the player's chip count.
     *
     * @return player's chip count
     */
    public int getChips() {
        return chips;
    }
    
    /**
     * Sets the player's chip count.
     *
     * @param c the new chip count
     */
    public void setChips(int c) {
        chips = c;
    }
    
    /**
     * Removes a number of chips from the player.
     *
     * @param c the number of chips to remove
     */
    public void removeChips(int c) {
        chips -= c;
    }
    
    /**
     * Adds a number of chips from the player.
     *
     * @param c the number of chips to add
     */
    public void addChips(int c) {
        chips += c;
    }
    
    /**
     * Gets the player id.
     *
     * @return the player id
     */
    public int getId() {
        return id;
    }
    
    /**
     * Give a player a card.
     *
     * @param c the card to add
     */
    public void addCard(Card c) {
        if (hole.size() == 2) {
            throw new IllegalStateException();
        }
        hole.add(c);
    }
    
    /**
     * Removes the hole cards.
     */
    public void removeHoleCards() {
        hole.clear();
    }
    
    /**
     * Gets the player's action.
     *
     * @return the action
     */
    public abstract Action getAction();
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ("" + id);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Player p) {
        if (this.equals(p)) {
            return 0;
        } else if (this.getId() > p.getId()) {
            return 1;
        } else {
            return -1;
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Player))
            return false;
        Player other = (Player) obj;
        if (id != other.id)
            return false;
        return true;
    }
    
}