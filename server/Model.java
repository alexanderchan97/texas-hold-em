package server;

import poker.GameModel;

public class Model {

    private GameModel gm;
    private GameServerModel gsm;
    
    public Model() {
        gm = new GameModel();
        gsm = new GameServerModel();
    }
    
    public GameModel getGameModel() {
        return gm;
    }
    
    public GameServerModel getGameServerModel() {
        return gsm;
    }
    
    public void setGameModel(GameModel gm) {
        this.gm = gm;
    }
    
    public void setGameServerModel(GameServerModel gsm) {
        this.gsm = gsm;
    }
    
}
