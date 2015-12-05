package server;

import java.util.Collection;

public interface GameServerApi {
    
    Broadcast registerUser(int userId);

    Broadcast deregisterUser(int userId);

}
