package poker;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class GameTest {

    Deck d;
    Collection<Card> hand;
    
    @Before
    public void setUp() throws Exception {
        d = new Deck();
        d.shuffle();
        hand = new TreeSet<Card>();
    }

    @Test
    public void testDeckConstructor() {
        assertEquals("deck size", 52, d.size());
    }
    
    @Test
    public void testDealFiveCardHand() {
        for (int i = 0; i < 5; ++i) {
            hand.add(d.deal());
        }
        assertEquals("deck size", 47, d.size());
        assertEquals("hand size", 5, hand.size());
    }
    
    
    // Helper method for creating hands with any number of cards
    public void addCards(String cards) {
        String[] sArray = cards.split(";");
        for (String s : sArray) {
            hand.add(new Card(s));
        }
    }
    
    // Tests for a 5 card hand
    @Test
    public void testStraightFlush() {
        addCards("7h;Th;9h;Jh;8h");
        assertEquals("straight flush", Hand.STRAIGHT_FLUSH, Hand.evaluateHand(hand));
    }
    
    @Test
    public void testFourOfAKind() {
        addCards("As;Ac;Ad;Ah;7s");
        assertEquals("four of a kind", Hand.FOUR_OF_A_KIND, Hand.evaluateHand(hand));
    }
    
    @Test
    public void testFullHouse() {
        addCards("7s;Kh;7d;Kc;Kd");
        assertEquals("full house", Hand.FULL_HOUSE, Hand.evaluateHand(hand));
    }
    
    @Test
    public void testFlush() {
        addCards("Ac;7c;6c;2c;Tc");
        assertEquals("flush", Hand.FLUSH, Hand.evaluateHand(hand));
    }
    
    @Test
    public void testStraight() {
        addCards("Ts;8c;Jh;9d;Qs");
        assertEquals("straight", Hand.STRAIGHT, Hand.evaluateHand(hand));
    }
    
    @Test
    public void testBroadway() {
        addCards("Qh;As;Jd;Td;Kd");
        assertEquals("broadway", Hand.STRAIGHT, Hand.evaluateHand(hand));
    }
    
    @Test
    public void testWheel() {
        addCards("Ah;3c;5s;4s;2s");
        assertEquals("wheel", Hand.STRAIGHT, Hand.evaluateHand(hand));
    }
    
    @Test
    public void testThreeOfAKind() {
        addCards("2h;4d;2s;9d;2c");
        assertEquals("three of a kind", Hand.THREE_OF_A_KIND, Hand.evaluateHand(hand));
    }
    
    @Test
    public void testTwoPair() {
        addCards("Qs;Jd;Th;Jc;Tc");
        assertEquals("two pair", Hand.TWO_PAIR, Hand.evaluateHand(hand));
    }
    
    @Test
    public void testOnePair() {
        addCards("Tc;9h;3s;3d;Ac");
        assertEquals("one pair", Hand.ONE_PAIR, Hand.evaluateHand(hand));
    }
    
    // Tests for subsets
    @Test
    public void testSubsetSizeZero() {
        addCards("As;Ks;Qs;Js;Ts;9s;8s");
        assertEquals("1 subset size zero", 1, Hand.generateSubsets((Set<Card>) hand, 0).size());
    }
    
    @Test
    public void testSubsetSizeOne() {
        addCards("As;Ks;Qs;Js;Ts;9s;8s");
        assertEquals("7 subsets size one", 7, Hand.generateSubsets((Set<Card>) hand, 1).size());
    }
    
    @Test public void testSubsetSizeFive() {
        addCards("As;Ks;Qs;Js;Ts;9s;8s");
        assertEquals("21 subsets size 5", 21, Hand.generateSubsets((Set<Card>) hand, 5).size());
    }
    
    // Tests for a 7 card collection
    @Test
    public void testStraightFlushSeven() {
        addCards("7h;Th;9h;2s;4h;Jh;8h");
        assertEquals("straight flush", Hand.STRAIGHT_FLUSH, Hand.evaluateHand(hand));
    }

}
