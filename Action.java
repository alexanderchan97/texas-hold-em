
public abstract class Action {

    protected int player;
    
    protected Action(int player) {
        this.player = player;
    }
    
    public int getSenderId() {
        return player;
    }
    
}

class CheckAction extends Action {
    
    public CheckAction(int player) {
        super(player);
    }
    
    @Override
    public String toString() {
        return (player + "check");
    }
    
}

class BetAction extends Action {

    private int betValue;
    
    BetAction(int player, int betValue) {
        super(player);
        this.betValue = betValue;
    }
    
}