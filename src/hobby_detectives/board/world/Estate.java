package hobby_detectives.board.world;

import hobby_detectives.data.RoomType;
import hobby_detectives.data.WeaponType;
import hobby_detectives.engine.Position;
import hobby_detectives.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Estate extends Tile {
    public Estate(Position position, int width, int height) {
        super(position);
        this.width = width;
        this.height = height;
    }

    public RoomType type;
    public WeaponType weapon;
    public final int width;
    public final int height;

    public List<Position> doors = new ArrayList<>();
    public List<Player> players = new ArrayList<>();
}
