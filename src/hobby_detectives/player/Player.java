package hobby_detectives.player;

import hobby_detectives.board.world.Tile;
import hobby_detectives.data.CharacterType;
import hobby_detectives.game.Card;

import java.util.List;

/**
 * Represents a player in the game.
 */
public class Player {
    private final CharacterType character;
    //not aprt of original plan but I need this to figure out where tf the player is without looping over the entire baord and checking each tile
    private Tile currentTile;
    private final List<Card> cards;

    private boolean allowedToGuess = true;
    private boolean hasUsedSolveAttempt = false;

    private final String color;

    public boolean getAllowedToGuess() {
        return allowedToGuess;
    }

    public void setAllowedToGuess(boolean n) {
        allowedToGuess = n;
    }

    public Player(CharacterType character, List<Card> cards, String color) {
        this.cards = cards;
        this.character = character;
        this.color = color;
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public CharacterType getCharacter() {
        return this.character;
    }

    public String getColor() {
        return this.color;
    }

    public Tile getTile() {
        return this.currentTile;
    }

    public void setTile(Tile t) {
        this.currentTile = t;
    }

    public void addCard(Card c) {
        cards.add(c);
    }

    public void playerHasAttemptedToSolve() {
        this.hasUsedSolveAttempt = true;
    }

    public String toString() {
        return this.getCharacter().toString();
    }
}
