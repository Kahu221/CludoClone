package hobby_detectives.engine;

public abstract class BoardObject extends GameObject {
    protected Position position;
    public BoardObject(Position position) {
        this.position = position;
    }

    public Position getPosition() { return this.position; }
    public void setPosition(Position n) { this.position = n; }

    public abstract String render();
}
