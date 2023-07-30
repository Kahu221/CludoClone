package hobby_detectives.board.world;

import hobby_detectives.data.RoomType;
import hobby_detectives.data.WeaponType;
import hobby_detectives.engine.Position;
import hobby_detectives.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Estate extends Tile {
    public Estate(Position position, int width, int height, RoomType type, WeaponType weapon) {
        super(position);
        this.width = width;
        this.height = height;
        this.weapon = weapon;
        this.type = type;
    }

    public RoomType type;
    public WeaponType weapon;
    public final int width;
    public final int height;

    public List<Position> doors = new ArrayList<>();
    public List<Player> players = new ArrayList<>();

    @Override
    public String render() {
        return "|" + this.type.name().charAt(0);
    }

    public Collection<Tile> generateFillTiles() {
        var fills = new ArrayList<Tile>();
        for (int ix = 0; ix < this.width; ix++) {
            for (int iy = 0; iy < this.height; iy++) {
                if (ix == 0 && iy == 0) continue;
                fills.add(new Tile(new Position(this.position.x()+ix, this.position.y()+iy)) {
                   @Override
                   public String render() {
                       return Estate.this.render();
                   }
                });
            }
        }
        return fills;
    }
}
