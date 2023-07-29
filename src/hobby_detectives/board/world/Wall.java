package hobby_detectives.board.world;

import hobby_detectives.engine.Position;

public class Wall extends Tile{
    public Wall(Position position){
        super(position);
    }
    @Override
    public String render(){
        return "#";
    }
}
