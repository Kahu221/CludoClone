package hobby_detectives.player;

import hobby_detectives.board.world.Tile;
import hobby_detectives.data.CharacterType;
import hobby_detectives.game.Card;

import java.util.ArrayList;

public class Player {
    private final CharacterType character;
    //not aprt of original plan but i need this to figure out where tf the player is without looping over the entire baord and checking each tile
    private Tile currentTile;
    private ArrayList<Card> cards;

    public Player(CharacterType character) {

        this.character = character;
    }

    public CharacterType getCharacter() {
        return this.character;
    }

    public Tile getTile() {
        return this.currentTile;
    }

    public void setTile(Tile t) {
        this.currentTile = t;
    }
}
