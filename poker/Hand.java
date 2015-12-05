package poker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public enum Hand {

    STRAIGHT_FLUSH   (1),
    FOUR_OF_A_KIND   (2),
    FULL_HOUSE       (3),
    FLUSH            (4),
    STRAIGHT         (5),
    THREE_OF_A_KIND  (6),
    TWO_PAIR         (7),
    ONE_PAIR         (8),
    HIGH_CARD        (9);
    
    private final int val;
    
    Hand (int i) { 
        val = i;
    }
    
    public int toInt() {
        return val;
    }
    
    public static Hand evaluateHand(Collection<Card> cs) {
        Set<Card> cards = new HashSet<Card>(cs);
        if (cards.size() < 5) {
            throw new IllegalArgumentException();
        } else if (cards.size() > 5) {
            Set<Set<Card>> fiveCardHands = generateSubsets(cards, 5);
            List<Hand> ccRanks = new ArrayList<Hand>();
            for (Collection<Card> cc : fiveCardHands) {
                System.out.println(cc.size());
                Hand ccStatus = evaluateHand(cc);
                ccRanks.add(ccStatus);
            }
            return Collections.min(ccRanks);
        }
        List<Card> cList = new ArrayList<Card>(cards);
        Collections.sort(cList);
        String cardString = "";
        for (Card c : cList) {
            cardString += c.toString();
        }
        // Match a hand with repeating ranks
        if (cardString.matches(
                "(?:[\\dTJQKA].)?([\\dTJQKA])[shcd](\\1[shcd]){3}(?:[\\dTJQKA].)?")) {
            return Hand.FOUR_OF_A_KIND;
        } else if (cardString.matches(
                "([\\dTJQKA])[shcd](?:\\1[shcd]){1,2}([\\dTJQKA])[shcd](?:\\2[shcd]){1,2}")) {
            return Hand.FULL_HOUSE;
        } else if (cardString.matches(
                "(?:..){0,2}([\\dTJQKA])[shcd](\\1[shcd]){2}(?:..){0,2}")) {
            return Hand.THREE_OF_A_KIND;
        } else if (cardString.matches(
                "(?:..)?([\\dTJQKA])[shcd]\\1[shcd](?:..)?([\\dTJQKA])[shcd]\\2[shcd](?:..)?")) {
            return Hand.TWO_PAIR;
        } else if (cardString.matches(
                "(?:..){0,3}([\\dTJQKA])[shcd]\\1[shcd](?:..){0,3}")) {
            return Hand.ONE_PAIR;
        }
        
        // Check if the hand is a flush
        boolean isFlush = false;
        if (cardString.matches(".([shcd])(?:.\\1){4}")) {
            isFlush = true;
        }
        
        // Check if the hand is a straight
        boolean isStraight = false;
        List<Integer> rankList = new ArrayList<Integer>();
        for (Card c : cList) {
            rankList.add(c.getRank().toInt());
        }
        Collections.sort(rankList);
        if (rankList.get(4) - rankList.get(0) == 4 ||
                (rankList.get(4) == 14 && rankList.get(3) == 5)) {
            isStraight = true;
        }
        
        if (isFlush && isStraight) {
            return Hand.STRAIGHT_FLUSH;
        } else if (isFlush) {
            return Hand.FLUSH;
        } else if (isStraight) {
            return Hand.STRAIGHT;
        } else {
            return Hand.HIGH_CARD;
        }
    }
    
    public static <T> Set<Set<T>> generateSubsets(Set<T> ts) {
        Set<Set<T>> retVal = new HashSet<Set<T>>();
        for (int i = ts.size(); i >=0; --i) {
            Set<Set<T>> smallerSubsets = generateSubsets(ts, i);
            for (Set<T> st : smallerSubsets) {
                retVal.add(st);
            }
        }
        return retVal;
    }
    
    public static <T> Set<Set<T>> generateSubsets(Set<T> ts, int n) {
        Set<Set<T>> retVal = new HashSet<Set<T>>();
        if (n == 0) {
            retVal.add(new HashSet<T>());
            return retVal;
        } else {
            Set<Set<T>> smallerSubsets = generateSubsets(ts, n - 1);
            for (Set<T> st : smallerSubsets) {
                Set<T> missingElements = new HashSet<T>(ts);
                missingElements.removeAll(st);
                for (T t : missingElements) {
                    Set<T> newSubset = new HashSet<T>(st);
                    newSubset.add(t);
                    retVal.add(newSubset);
                }
            }
            return retVal;
        }
    }
    
}
