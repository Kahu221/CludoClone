package hobby_detectives.player;

import hobby_detectives.data.CharacterType;

public class Player {
    private final CharacterType character;

    public CharacterType getCharacter() { return this.character; }

    public Player(CharacterType character) {
        this.character = character;
    }
}
