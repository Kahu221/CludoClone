package hobby_detectives.gui.controller.pathing;

import hobby_detectives.board.Board;
import hobby_detectives.board.world.Estate;
import hobby_detectives.board.world.UnreachableArea;
import hobby_detectives.engine.Position;

import java.util.*;

/**
 * Utilities regarding the implementation of the A* path-finding algorithm in the game.
 */
public class PathAlgorithm {

    /**
     * Attempts to find the shortest path between two positions on the board.
     * @param board The board.
     * @param currentPosition The origin.
     * @param goal The destination.
     * @return A list of edges required to traverse from the origin to the destination, or an empty list if no valid path was found.
     */
    public static List<BoardEdge> findShortestPath(Board board, Position currentPosition, Position goal) {
        Queue<TraversalPath> pq = new PriorityQueue<>();
        Map<Position, BoardEdge> backPointers = new HashMap<>();
        pq.offer(new TraversalPath(currentPosition, null, 0, distanceTo(currentPosition, goal)));
        Set<Position> visited = new HashSet<>();

        while (!pq.isEmpty()) {
            TraversalPath fringe = pq.poll();
            if (!visited.contains(fringe.current())) {
                visited.add(fringe.current());

                backPointers.put(fringe.current(), fringe.lastMove()); //add to backPointers with stop pointing to an edge

                if (fringe.current().equals(goal)) {
                    return reconstruct(currentPosition, goal, backPointers);
                }
                for (BoardEdge e : computeEdges(board, fringe.current())) {
                    if (!backPointers.containsKey(e.to())) {
                        Position neighbour = e.to();
                        var lengthToNeighbour = fringe.distanceFromStart() + 1;
                        var estimateTotalPath = lengthToNeighbour + distanceTo(neighbour, goal);
                        pq.add(new TraversalPath(neighbour, e, lengthToNeighbour, estimateTotalPath));
                    }
                }
            }
        }
        return List.of();
    }

    public static Set<BoardEdge> computeEdges(Board board, Position origin) {
        var set = new HashSet<BoardEdge>();
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                var destination = origin.add(new Position(x,y));
                var dist = destination.distance(origin);
                if (dist <= 1 && dist > 0) {
                    var tile = board.read(destination);
                    if (tile != null
                            && !(tile instanceof UnreachableArea)
                            && !(tile instanceof Estate.EstateFillTile)
                            && !(tile instanceof Estate)
                            && tile.occupant.isEmpty()) {
                        set.add(new BoardEdge(origin, destination));
                    }
                }

            }
        }
        return set;
    }

    public static List<BoardEdge> reconstruct(Position start, Position goal, Map<Position, BoardEdge> backpointers) {
        if (start == goal) return new ArrayList<>();
        var output = new ArrayList<BoardEdge>();
        BoardEdge e = backpointers.get(goal);
        while (true) {
            BoardEdge to = e;
            BoardEdge from = backpointers.get(to.from());
            output.add(to);
            if (to.from().equals(start)) {
                Collections.reverse(output);
                return output;
            }
            e = from;
        }
    }

    public static int distanceTo(Position from, Position goal) {
        return Math.abs(goal.x() - from.x()) + Math.abs(goal.y() - from.y());
    }
}
