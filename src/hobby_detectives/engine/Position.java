package hobby_detectives.engine;

public record Position(int x, int y) {
    public Position add(Position position) {
        return new Position(this.x + position.x, this.y+position.y);
    }
    public Position subtract(Position position) {
        return new Position(this.x-position.x, this.y-position.y);
    }
}
