package hobby_detectives.board.world;

import hobby_detectives.data.CharacterColors;
import hobby_detectives.engine.Position;

/**
 * Represents a blocked area that cannot be traversed in the world.
 */
public class UnreachableArea extends Tile {
    public UnreachableArea(Position position) {
        super(position);
    }

    @Override
    public String render() {
        return "|" + CharacterColors.BOLD.getColor() + "#" + CharacterColors.RESET.getColor();
    }
}
