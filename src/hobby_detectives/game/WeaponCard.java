package hobby_detectives.game;

import hobby_detectives.data.WeaponType;

public class WeaponCard extends Card {
    public final WeaponType weapon;

    public WeaponCard(WeaponType weapon) {
        this.weapon = weapon;
    }

    public boolean equals(Card c) {
        return c instanceof WeaponCard cx && cx.weapon.equals(weapon);
    }

    public String toString() {
        return weapon.toString();
    }
}
