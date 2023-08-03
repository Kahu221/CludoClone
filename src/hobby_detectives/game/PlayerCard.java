package hobby_detectives.game;

import hobby_detectives.player.Player;

public class PlayerCard extends Card {
    public final Player player;

    public PlayerCard(Player player) {
        this.player = player;
    }

    public boolean equals(Card c) {
        return c instanceof PlayerCard cx && cx.player.getCharacter().equals(player.getCharacter());
    }

    public String toString() {
        return player.getCharacter().toString();
    }
}
