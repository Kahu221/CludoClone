package hobby_detectives.board.world;

import hobby_detectives.data.CharacterColors;
import hobby_detectives.engine.BoardObject;
import hobby_detectives.engine.Position;
import hobby_detectives.player.Player;

import javax.swing.*;
import java.util.Optional;

/**
 * Represents a tile in the game world.
 */
public class Tile extends BoardObject {
    /**
     * The player currently occupying this tile.
     */
    public Optional<Player> occupant = Optional.empty();

    public Tile(Position position) {
        super(position);
    }

    /**
     * Sets the player occupying this tile.
     */
    public void setPlayer(Player p) {
        if (p == null) occupant = Optional.empty();
        else occupant = Optional.of(p);
    }

    @Override
    public String render() {
        if (occupant.isPresent()) return Character.toString(occupant.get().getCharacter().toString().toLowerCase().charAt(0));
        return "_";
    }

    @Override
    public String toString() {
        return "Tile at " + this.position + ": " + this.render();
    }
}
