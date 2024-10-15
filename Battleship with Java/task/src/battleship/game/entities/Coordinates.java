package battleship.game.entities;

public record Coordinates(char vertical, int horizontal) {
    @Override
    public String toString() {
        return String.format("%c%d", vertical, horizontal);
    }
}
