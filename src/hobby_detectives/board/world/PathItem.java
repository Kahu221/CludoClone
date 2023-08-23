package hobby_detectives.board.world;

import java.nio.file.Path;

public class PathItem implements Comparable<PathItem> {

    private final Tile currentTile; //currentStop we are at
    private final Edge fromEdge; //edge used to get to current position
    private final int distFromStart; //distance from start to currentStop
    private final int distToEnd;

    public PathItem(Tile currentTile, Edge fromEdge, int distFromStart, int distToEnd) {
        this.currentTile = currentTile;
        this.fromEdge = fromEdge;
        this.distFromStart = distFromStart;
        this.distToEnd = distToEnd;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public Edge getFromEdge() {
        return fromEdge;
    }

    public int getDistFromStart() {
        return distFromStart;
    }

    public int getDistToEnd() {
        return distToEnd;
    }

    @Override
    public int compareTo(PathItem o) {
        return 0;
    }
}
