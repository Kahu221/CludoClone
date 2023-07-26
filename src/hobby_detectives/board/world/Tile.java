package hobby_detectives.board.world;

import hobby_detectives.engine.BoardObject;
import hobby_detectives.engine.Position;
import hobby_detectives.player.Player;

import java.util.Optional;

public class Tile extends BoardObject {
    public Optional<Player> occupant = Optional.empty();

    public Tile(Position position) {
        super(position);
    }

    @Override
    public String render() {
        return "_";
    }
}
