package hobby_detectives.gui.controller.pathing;

import hobby_detectives.engine.Position;

public record BoardEdge(Position from, Position to) {
    @Override
    public String toString() {
        return from + "->" + to;
    }
}
