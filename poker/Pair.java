package poker;

/**
 * A {@code Pair} contains two elements of any type in a given order.
 *
 * @param <F> the generic type
 * @param <S> the generic type
 */
public class Pair<F, S> {

    private final F f;
    private final S s;
    
    /**
     * Instantiates a new pair.
     *
     * @param f the first element of the pair
     * @param s the second element of the pair
     */
    public Pair(F f, S s) {
        this.f = f;
        this.s = s;
    }
    
    /**
     * Gets the first element of the pair.
     *
     * @return the first element of the pair
     */
    public F getFirst() {
        return f;
    }
    
    /**
     * Gets the second element of the pair.
     *
     * @return the second element of the pair
     */
    public S getSecond() {
        return s;
    }
    
}
