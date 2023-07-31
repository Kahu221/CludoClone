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

    public void setPlayer(Player p) {
        if (p == null) occupant = Optional.empty();
        else occupant = Optional.of(p);
    }

    @Override
    public String render() {
        if (occupant.isPresent()) return "|" + occupant.get().getCharacter().toString().charAt(0);
        return "|_";
    }

    @Override
    public String toString() {
        return "Tile at " + this.position + ": " + this.render();
    }
}
