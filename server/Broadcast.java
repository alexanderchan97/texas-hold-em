package server;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import poker.Action;
import poker.GameError;
import poker.Player;

public class Broadcast {

    private final Map<Integer, List<String>> responses;

    protected Broadcast() {
        responses = new TreeMap<Integer, List<String>>();
    }

    protected void addResponse(Player p, String response) {
        if (!responses.containsKey(p.getId())) {
            responses.put(p.getId(), new LinkedList<String>());
        }
        List<String> userResponses = responses.get(p.getId());
        if (!userResponses.contains(response)) {
            userResponses.add(response);
        }
    }

    public static Broadcast okay(Action action, Set<Player> recipients) {
        Broadcast broadcast = new Broadcast();
        for (Player p : recipients) {
            broadcast.addResponse(p, action.toString());
        }
        return broadcast;
    }

    public static Broadcast error(Action action, GameError error) {
        if (error == GameError.OKAY) {
            throw new IllegalArgumentException();
        }
        Broadcast broadcast = new Broadcast();
        Player recipient = action.getPlayer();
        int errorCode = error.getCode();
        String response = String.format(":%s ERROR %d", recipient.toString(), errorCode);
        broadcast.addResponse(recipient, response);
        return broadcast;
    }

    public Map<Integer, List<String>> getResponses() {
        Map<Integer, List<String>> playerResponses = new TreeMap<>();
        for (Map.Entry<Integer, List<String>> e : responses.entrySet()) {
            playerResponses.put(e.getKey(), e.getValue());
        }
        return playerResponses;
    }
    
}