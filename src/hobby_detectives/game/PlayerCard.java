package hobby_detectives.game;

import hobby_detectives.data.CharacterType;

public class PlayerCard extends Card {
    public final CharacterType character;

    public PlayerCard(CharacterType character) {
        this.character = character;
    }

    public boolean equals(Card c) {
        return c instanceof PlayerCard cx && cx.character.equals(character);
    }

    public String toString() {
        return character.toString();
    }
}
