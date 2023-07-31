package hobby_detectives.board.world;

import hobby_detectives.engine.Position;

public class UnreachableArea extends Tile {
    public UnreachableArea(Position position) {
        super(position);
    }

    @Override
    public String render() {
        return "  ";
    }
}
