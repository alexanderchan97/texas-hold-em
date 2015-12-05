package poker;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

/**
 * {@code Card} is a type containing a rank and a suit.
 *
 */
public class Card implements Comparable<Card> {

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
    
    /**
     * Instantiates a new card.
     *
     * @param s the string representation of the card
     */
    public Card(String s) {
        if (!s.matches("([2-9]|T|J|Q|K|A)(s|h|c|d)")) {
            throw new IllegalArgumentException();
        }
        String rankStr = s.substring(0, 1);
        String suitStr = s.substring(1);
        rank = Rank.fromString(rankStr);
        suit = Suit.fromString(suitStr);
        
    }
    
    /**
     * Gets the rank.
     *
     * @return the rank
     */
    public Rank getRank() {
        return rank;
    }
    
    /**
     * Gets the suit.
     *
     * @return the suit
     */
    public Suit getSuit() {
        return suit;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return (rank.toString() + suit.toString());
    }
    
    public Point getSrcCoords() {
        return (new Point(rank.getX(), suit.getY()));
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Card c) {
        if (this.equals(c)) {
            return 0;
        } else if (this.rank.toInt() > c.rank.toInt()) {
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
        result = prime * result + ((rank == null) ? 0 : rank.hashCode());
        result = prime * result + ((suit == null) ? 0 : suit.hashCode());
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
        if (!(obj instanceof Card))
            return false;
        Card other = (Card) obj;
        if (rank != other.rank)
            return false;
        if (suit != other.suit)
            return false;
        return true;
    }
    
}
