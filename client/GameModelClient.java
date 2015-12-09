package client;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Observable;
import java.util.TreeSet;
import poker.Card;

public class GameModelClient extends Observable {

    private int pot;
    private int blindSmall;
    private int blindBig;
    private int currBet;
    private int round;
    private int oppChips;
    private boolean isTurn;
    private Collection<Card> board;
    
    public GameModelClient() {
        pot = 0;
        blindSmall = 0;
        blindBig = 0;
        currBet = 0;
        round = 0;
        isTurn = false;
        board = new LinkedList<Card>();
    }
    
    public GameModelClient(int pot, int blindSmall, int blindBig, boolean isTurn) {
        this.pot = pot;
        this.blindSmall = blindSmall;
        this.blindBig = blindBig;
        currBet = 0;
        round = 1;
        this.isTurn = isTurn;
        board = new TreeSet<Card>();
    }
    
    public int getPot() {
        return pot;
    }
    
    public void setPot(int c) {
        if (c == pot) {
            return;
        }
        pot = c;
        setChanged();
        notifyObservers();
    }
    
    public int getSmallBlind() {
        return blindSmall;
    }
    
    public void setSmallBlind(int c) {
        if (c == blindSmall) {
            return;
        }
        blindSmall = c;
        setChanged();
        notifyObservers();
    }
    
    public int getBigBlind() {
        return blindBig;
    }
    
    public void setBigBlind(int c) {
        if (c == blindBig) {
            return;
        }
        blindBig = c;
        setChanged();
        notifyObservers();
    }
    
    public int getCurrentBet() {
        return currBet;
    }
    
    public void setCurrentBet(int c) {
        if (c == currBet) {
            return;
        }
        currBet = c;
        setChanged();
        notifyObservers();
    }
    
    public int getRound() {
        return round;
    }
    
    public void setRound(int r) {
        if (r == round) {
            return;
        }
        round = r;
        setChanged();
        notifyObservers();
    }
    
    public int getOppChips() {
        return oppChips;
    }
    
    public void setOppChips(int c) {
        if (c == oppChips) {
            return;
        }
        oppChips = c;
        setChanged();
        notifyObservers();
    }
    
    public boolean isTurn() {
        return isTurn;
    }
    
    public void setIsTurn(boolean t) {
        if (t == isTurn) {
            return;
        }
        isTurn = t;
        setChanged();
        notifyObservers();
    }
    
    public Collection<Card> getBoard() {
        return board;
    }
    
    public void addToBoard(String s) {
        if(s.equals("null")) {
            return;
        }
        String[] cards = s.split(";");
        for (String c : cards) {
            board.add(new Card(c));
        }
        setChanged();
        notifyObservers();
    }
    
    public void updateModel(String command) {
        if (!command.matches("\\d+ ((pot|blindSmall|blindBig|currBet|round|oppChips|board) .+)")) {
            throw new IllegalArgumentException();
        }
        String[] parsed = command.split(" ");
        int arg = 0;
        if (!parsed[1].equals("board")) {
            arg = Integer.parseInt(parsed[2]);
        }
        switch (parsed[1]) {
        case "pot":
            setPot(arg);
            break;
        case "blindSmall":
            setSmallBlind(arg);
            break;
        case "blindBig":
            setBigBlind(arg);
            break;
        case "currBet":
            setCurrentBet(arg);
            break;
        case "round":
            setRound(arg);
            break;
        case "oppChips":
            setOppChips(arg);
            break;
        case "board":
            addToBoard(parsed[2]);
            break;
        default:
            break;
        }
        
        
    }
    
    
}
