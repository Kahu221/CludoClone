package hobby_detectives.engine;

/**
 * Represents an object that lives on the board.
 * Board objects contain data like their position, and have a
 * method that gets the token to render to the screen.
 */
public abstract class BoardObject extends GameObject {
    protected Position position;

    public BoardObject(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position n) {
        this.position = n;
    }

    public abstract String render();
}
