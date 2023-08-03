package hobby_detectives.board.world;

import hobby_detectives.data.Direction;
import hobby_detectives.data.EstateType;
import hobby_detectives.data.WeaponType;
import hobby_detectives.engine.Position;
import hobby_detectives.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Estate extends Tile {
    public final int width;
    public final int height;
    public final List<Position> doors;
    public final List<Direction> doorDirections;
    public EstateType type;
    public WeaponType weapon;
    public List<Player> players = new ArrayList<>();
    public Estate(Position position, int width, int height,
                  EstateType type,
                  WeaponType weapon,
                  List<Position> doors,
                  List<Direction> doorDirections
                  ) {
        super(position);
        this.width = width;
        this.height = height;
        this.weapon = weapon;
        this.type = type;
        this.doors = doors;
        this.doorDirections = doorDirections;
    }

    @Override
    public String render() {
        return "|" + this.type.name().charAt(0);
    }

    /**
     * This method generates the "fill tiles" for an estate, because an estate is a multi-tile block.
     * Estates are always positioned in the top-left corner (origin), and fan out to fill the multi-tile area.
     */
    public Collection<Tile> generateFillTiles() {
        var fills = new ArrayList<Tile>();
        for (int ix = 0; ix < this.width; ix++) {
            for (int iy = 0; iy < this.height; iy++) {
                if (ix == 0 && iy == 0) continue;
                fills.add(new EstateFillTile(new Position(this.position.x() + ix, this.position.y() + iy), this));
            }
        }
        return fills;
    }

    public class EstateFillTile extends Tile {
        public final Estate parent;

        public EstateFillTile(Position position, Estate parent) {
            super(position);
            this.parent = parent;
        }

        @Override
        public String render() {
            if (doors.stream().anyMatch(e -> Estate.this.position.add(e).equals(this.position))) {
                return "|*";
            }
            if (this.occupant.isPresent()) return "|" + this.occupant.get().getCharacter().toString().charAt(0);
            return Estate.this.render();
        }
    }
}
