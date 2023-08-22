package hobby_detectives.engine;

/**
 * Represents an (x,y) 2D position vector in the game world.
 */
public record Position(int x, int y) {
    /**
     * Adds two vectors together.
     */
    public Position add(Position position) {
        return new Position(this.x + position.x, this.y + position.y);
    }

    /**
     * Subtracts the given vector from the current vector.
     */
    public Position subtract(Position position) {
        return new Position(this.x - position.x, this.y - position.y);
    }
}
