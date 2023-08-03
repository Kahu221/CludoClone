package hobby_detectives.data;

public enum CharacterColors {
    RED("\u001b[1m\u001b[31m"),
    GREEN("\u001b[1m\u001b[32m"),
    MAGENTA("\u001b[1m\u001b[35m"),
    BLUE("\u001b[1m\u001b[34m"),
    RESET("\u001b[1m\u001b[0m");

    private final String color;

    CharacterColors(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
