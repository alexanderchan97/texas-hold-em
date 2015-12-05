package server;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public final class GameServerModel implements GameServerApi {

    private Set<Integer> players;

    public GameServerModel() {
        players = new TreeSet<Integer>();
    }

    @Override
    public Broadcast registerUser(int userId) {
        int uniqueId = generateUniqueId();
        players.add(uniqueId);
        return null;
    }
    
    public int generateUniqueId() {
        if (players.isEmpty()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public Broadcast deregisterUser(int userId) {
        players.remove((Integer) userId);
        return null;
    }
    
}
