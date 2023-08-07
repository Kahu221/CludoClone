package hobby_detectives.game;

import hobby_detectives.data.EstateType;

/**
 * Represents a card that indicates an estate.
 */
public class EstateCard extends Card {
    public final EstateType estate;

    public EstateCard(EstateType estate) {
        this.estate = estate;
    }

    public boolean equals(Card c) {
        return c instanceof EstateCard cx && cx.estate.equals(estate);
    }

    public String toString() {
        return estate.toString();
    }
}
