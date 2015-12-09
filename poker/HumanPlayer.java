package poker;

public class HumanPlayer extends Player {

    private static final long serialVersionUID = 1L;

    public HumanPlayer(int id, int chips) {
        super(id, chips);
    }

    @Override
    public String getAction() {
        return action;
    }
    
    public void setAction(String s) {
        action = s;
    }

}
