package poker;
import java.util.Set;
import java.util.TreeSet;

import server.Broadcast;
import server.GameServerModel;

public abstract class Action {

    protected int playerId;
    protected Player player;
    
    protected Action(Player p) {
        playerId = p.getId();
        player = p;
    }
    
    public int getPlayerId() {
        return playerId;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public static Action stringToAction(String s) {
        if (!s.matches("\\d+ (check|bet \\d+|call \\d+|raise \\d+|fold)")) {
            throw new IllegalArgumentException();
        }
        String[] command = s.split(" ");
        switch (command[1]) {
        // TODO: implement
        case "check":
        case "bet":
        case "call":
        case "raise":
        case "fold":
        default:
        }
        return null;
    }

    public abstract int getValue();
    
}

abstract class GameAction extends Action {

    protected GameAction(Player player) {
        super(player);
    }
    
    public abstract Broadcast updateModel(GameModel model);
    
}

abstract class ServerAction extends Action {

    protected ServerAction(Player player) {
        super(player);
    }
    
    public abstract Broadcast updateModel(GameServerModel model);
    
}

class CheckAction extends GameAction {
    
    public CheckAction(Player player) {
        super(player);
    }
    
    @Override
    public String toString() {
        return (player + " check");
    }
    
    public int getValue() {
        return 0;
    }

    @Override
    public Broadcast updateModel(GameModel model) {
        if (!model.isValidAction(this, model.getLastAction())) {
            return Broadcast.error(this, GameError.CURRENT_BET_EXISTS);
        }
        model.addMatchedPlayer(player);
        Set<Player> recipients = new TreeSet<Player>(model.getPlayers());
        return Broadcast.okay(this, recipients);
    }
}

class BetAction extends GameAction {

    private int betValue;
    
    public BetAction(Player player, int betValue) {
        super(player);
        this.betValue = betValue;
    }
    
    @Override
    public String toString() {
        return (player + " bet " + betValue);
    }
    
    public int getValue() {
        return betValue;
    }

    @Override
    public Broadcast updateModel(GameModel model) {
        if (player.getChips() < betValue) {
            return Broadcast.error(this, GameError.NOT_ENOUGH_CHIPS);
        }
        if (!model.isValidAction(this, model.getLastAction())) {
            return Broadcast.error(this, GameError.BET_TOO_SMALL);
        }
        model.clearMatchedPlayers();
        model.addMatchedPlayer(player);
        model.addToPot(betValue);
        player.removeChips(betValue);
        Set<Player> recipients = new TreeSet<Player>(model.getPlayers());
        return Broadcast.okay(this, recipients);
    }
}

class CallAction extends GameAction {
    
    private int callValue;
    
    public CallAction(Player player, int callValue) {
        super(player);
        this.callValue = callValue;
    }
    
    @Override
    public String toString() {
        return (player + " call " + callValue);
    }
    
    public int getValue() {
        return callValue;
    }

    @Override
    public Broadcast updateModel(GameModel model) {
        // TODO Auto-generated method stub
        return null;
    }
}

class RaiseAction extends GameAction {
    
    // Follows the raise-to standard
    private int raiseValue;
    
    public RaiseAction(Player player, int raiseValue) {
        super(player);
        this.raiseValue = raiseValue;
    }
    
    @Override
    public String toString() {
        return (player + " raise " + raiseValue);
    }
    
    public int getValue() {
        return raiseValue;
    }

    @Override
    public Broadcast updateModel(GameModel model) {
        // TODO Auto-generated method stub
        return null;
    }
}

class FoldAction extends GameAction {
    
    public FoldAction(Player player) {
        super(player);
    }
    
    @Override
    public String toString() {
        return (player + " fold");
    }
    
    public int getValue() {
        return 0;
    }

    @Override
    public Broadcast updateModel(GameModel model) {
        // TODO Auto-generated method stub
        return null;
    }
}