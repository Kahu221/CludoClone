package hobby_detectives.board.world;

public class Edge {
    private final Tile fromTile;
    private final Tile toTile;

    public Edge(Tile fromTile, Tile toTile) {
        this.fromTile = fromTile;
        this.toTile = toTile;
    }

    public Tile getFromTile() {
        return fromTile;
    }

    public Tile getToTile() {
        return toTile;
    }

    public int getDistance() {
        return 1;
    }

//    @Override
//    public String toString() {
//        return fromTile.toString();
//    }
}
