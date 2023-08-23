package hobby_detectives.gui.controller.pathing;

import hobby_detectives.engine.Position;

public record TraversalPath(Position current, BoardEdge lastMove, int distanceFromStart, int estimatedDistanceToEnd) implements Comparable<TraversalPath> {
    @Override
    public int compareTo(TraversalPath o) {
        return Integer.compare(this.estimatedDistanceToEnd(), o.estimatedDistanceToEnd());
    }
}
