package poker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

// TODO: Auto-generated Javadoc
/**
 * The Class GameModel.
 */
public class GameModel {
    
    private final int startStack;
    private int blindSmall;
    private int blindBig;
    private int blindIncr;
    private int roundsToIncrBlind;
    private int currBet;
    private int pot;
    private int round;
    private Action lastAction = new CheckAction(null);
    private Player onButton;
    private int currPlayerIndex;
    private Deck deck;
    private Collection<Card> board = new TreeSet<Card>();
    private List<Player> players = new ArrayList<Player>();
    private Collection<Player> currPlaying = new TreeSet<Player>();
    
    // Handles whether or not the hand should proceed to the next round
    private boolean roundProceed;
    private Collection<Player> matched = new TreeSet<Player>();
    
    /**
     * Instantiates a new game model.
     */
    public GameModel() {
        startStack = 2000;
        blindSmall = 10;
        blindBig = 20;
        blindIncr = 2;
        currBet = 0;
        pot = 0;
        round = 1;
        deck = new Deck();
        deck.shuffle();
    }
    
    /**
     * Adds a player.
     *
     * @param p the player to add
     */
    public void addPlayer(Player p) {
        players.add(p);
        p.setChips(2000);
    }
    
    /**
     * Move the button.
     */
    public void moveButton() {
        if (onButton == null && players.isEmpty()) {
            throw new IllegalStateException();
        } else if (onButton == null) {
            onButton = players.get(0);
        } else {
            onButton = players.get(players.indexOf(onButton));
        }
    }
    
    /**
     * Deal each player two cards.
     */
    public void dealHoleCards() {
        int startIndex = players.indexOf(onButton);
        int j = startIndex;
        for (int i = 0; i < 2; ++i) {
            while (j < players.size()) {
                players.get(j).addCard(deck.deal());
                if (j == players.size() - 1) {
                    j = 0;
                    break;
                }
                ++j;
                if (j == startIndex) {
                    break;
                }
            }
        }
    }
    
    /**
     * Deal flop (three cards, one burned beforehand).
     */
    public void dealFlop() {
        deck.burn();
        for (int i = 0; i < 3; ++i) {
            board.add(deck.deal());
        }
    }
    
    /**
     * Deal single card (as in the turn or the river), one burned
     * beforehand.
     */
    public void dealSingle() {
        deck.burn();
        board.add(deck.deal());
    }
    
    /**
     * Increase the current player index by one. If we are already
     * at the last player in the list of players, we return to the
     * front of the list.
     */
    public void nextPlayer() {
        if (currPlaying.isEmpty()) {
            throw new IllegalStateException();
        }
        if (currPlayerIndex == players.size() - 1) {
            currPlayerIndex = 0;
        } else {
            ++currPlayerIndex;
        }
        if (!currPlaying.contains(players.get(currPlayerIndex))) {
            nextPlayer();
        }
    }
    
    /**
     * Adds a number of chips to the pot.
     *
     * @param c the value to add to the pot
     */
    public void addToPot(int c) {
        pot += c;
    }
    
    /**
     * Pay player what is in the pot.
     *
     * @param p the player to pay
     */
    public void payPotPlayer(Player p) {
        p.addChips(pot);
        pot = 0;
    }
    
    public void refundPlayer(Player p, int c) {
        p.addChips(c);
        pot -= c;
    }
    
    public int getSmallBlind() {
        return blindSmall;
    }
    
    public int getBigBlind() {
        return blindBig;
    }
    
    public int getPot() {
        return pot;
    }
    
    public Action getLastAction() {
        return lastAction;
    }
    
    /**
     * Checks if an action is valid.
     *
     * @param currAction the current action
     * @param lastAction the last action
     * @return true, if it is valid
     */
    public boolean isValidAction(Action currAction, Action lastAction) {
        if (lastAction instanceof CheckAction) {
            if (currAction instanceof CallAction) {
                return false;
            }
        } else if (lastAction instanceof BetAction) {
            if (currAction instanceof CheckAction || currAction instanceof BetAction) {
                return false;
            } else if (currAction instanceof RaiseAction) {
                if (currAction.getValue() - lastAction.getValue() < getSmallBlind()) {
                    return false;
                }
            }
        } else if (lastAction instanceof CallAction) {
            if (currAction instanceof CheckAction || currAction instanceof BetAction) {
                return false;
            } else if (currAction instanceof RaiseAction) {
                if (currAction.getValue() - lastAction.getValue() < getSmallBlind()) {
                    return false;
                }
            }
        } else if (lastAction instanceof RaiseAction) {
            if (currAction instanceof CheckAction || currAction instanceof BetAction) {
                return false;
            } else if (currAction instanceof RaiseAction) {
                if (currAction.getValue() - lastAction.getValue() < getSmallBlind()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public Collection<Player> getMatchedPlayers() {
        return matched;
    }
    
    public void addMatchedPlayer(Player p) {
        matched.add(p);
    }
    
    public void clearMatchedPlayers() {
        matched.clear();
    }
    
    public Collection<Player> getPlayers() {
        Collection<Player> ret = players;
        return ret;
    }
    
    /**
     * Play round.
     */
    public void playRound() {
        if (players.isEmpty() || players.size() == 1) {
            throw new IllegalStateException();
        }
        currPlaying = new TreeSet<Player>(players);
        
        // Create a new deck, and deal cards to each player.
        deck = new Deck();
        deck.shuffle();
        dealHoleCards();
        
        // Two players to the left of the button pay the blinds.
        // Third player to the left of the button acts first.
        currPlayerIndex = players.indexOf(onButton);
        nextPlayer();
        players.get(currPlayerIndex).removeChips(blindSmall);
        addToPot(blindSmall);
        nextPlayer();
        players.get(currPlayerIndex).removeChips(blindBig);
        addToPot(blindBig);
        nextPlayer();
        currBet = blindBig;
        
        // While there is more than one player in the hand, keep
        // querying each players action in turn.
        while (currPlaying.size() > 1) {
            roundProceed = true;
            Action a = players.get(currPlayerIndex).getAction();
            if (!this.isValidAction(a, lastAction)) {
                // Ask for another action
            }
            if (a instanceof FoldAction) {
                players.get(currPlayerIndex).removeHoleCards();
                currPlaying.remove(players.get(currPlayerIndex));
                matched.remove(players.get(currPlayerIndex));
                nextPlayer();
            } else if (a instanceof CheckAction) {
                matched.add(players.get(currPlayerIndex));
                nextPlayer();
            } else if (a instanceof CallAction) {
                players.get(currPlayerIndex).removeChips(a.getValue());
                addToPot(a.getValue());
                matched.add(players.get(currPlayerIndex));
                nextPlayer();
            } else if (a instanceof BetAction) {
                players.get(currPlayerIndex).removeChips(a.getValue());
                addToPot(a.getValue());
                currBet = a.getValue();
                roundProceed = false;
                matched.clear();
                matched.add(players.get(currPlayerIndex));
                nextPlayer();
            } else if (a instanceof RaiseAction) {
                players.get(currPlayerIndex).removeChips(a.getValue());
                addToPot(a.getValue());
                currBet = a.getValue();
                roundProceed = false;
                matched.clear();
                matched.add(players.get(currPlayerIndex));
                nextPlayer();
            }
            if (matched.size() == currPlaying.size()) {
                dealFlop();
            }
        }
    }
    
    @Override
    public String toString() {
        String b = "";
        for (Card c : board) {
            b += c.toString();
            b += " ";
        }
        b = b.trim();
        
        return ("_ pot " + pot + "\n" + 
                "_ blindSmall " + blindSmall + "\n" + 
                "_ blindBig " + blindBig + "\n" + 
                "_ currBet " + currBet + "\n" + 
                "_ round " + round + "\n" + 
                "_ board " + b);
        
        
    }
    
}
