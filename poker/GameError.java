package poker;

public enum GameError {

    OKAY(200),
    NO_CURRENT_BET(300),
    CURRENT_BET_EXISTS(301),
    BET_TOO_SMALL(302),
    CALL_WRONG_AMOUNT(303),
    NOT_ENOUGH_CHIPS(304);
    
    private final int val;
    
    GameError(int i) {
        val = i;
    }
    
    public int getCode() {
        return val;
    }
    
}
