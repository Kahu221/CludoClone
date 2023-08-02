package hobby_detectives.data;

public enum Direction {
    LEFT('l'),
    RIGHT('r'),
    UP('u'),
    DOWN('d');
    private final Character label;
    Direction(Character label) {
        this.label = label;
    }
    public Character label(){
        return this.label;
    }
}
