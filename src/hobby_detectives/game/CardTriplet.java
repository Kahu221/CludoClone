package hobby_detectives.game;

public record CardTriplet(WeaponCard weapon, EstateCard estate, PlayerCard player) {
    public boolean contains(Card card) {
        return weapon.equals(card) || estate.equals(card) || player.equals(card);
    }

    @Override
    public String toString() {
        return "[" + player + ", using " + weapon + " in " + estate + "]";
    }
}