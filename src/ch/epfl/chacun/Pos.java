package ch.epfl.chacun;

/**
 * Record that represent a position.
 *
 * @param x coordinate x
 * @param y coordinate y
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public record Pos(int x, int y) {
    public final static Pos ORIGIN = new Pos(0, 0);

    /**
     * Translates the position.
     *
     * @param dX delta x
     * @param dY delta y
     * @return translated position (Pos)
     */
    public Pos translated(int dX, int dY){
        return new Pos(this.x + dX, this.y + dY);
    }

    /**
     * Gives the neighbor position.
     *
     * @param direction the direction in which we want the neighbor position
     * @return the neighbor position (Pos)
     */
    public Pos neighbor(Direction direction){
        return switch (direction) {
            case N -> new Pos(this.x, this.y - 1);
            case E -> new Pos(this.x + 1, this.y);
            case S -> new Pos(this.x, this.y + 1);
            case W -> new Pos(this.x - 1, this.y);
        };
    }
}
