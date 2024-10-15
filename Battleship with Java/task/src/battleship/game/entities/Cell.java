package battleship.game.entities;

public class Cell {
    public enum Status {
        AVAILABLE('~'),
        UNAVAILABLE('~'),
        BATTLE_SHIP('O'),
        HIT('X'),
        MISS('M');

        private final char symbol;

        Status(char symbol) {
            this.symbol = symbol;
        }

        public char getSymbol() {
            return this.symbol;
        }
    }

    private final Coordinates coordinates;
    private Cell.Status status;
    private BattleShip battleShip;

    public Cell(char vertical, int horizontal) {
        this.coordinates = new Coordinates(vertical, horizontal);
        this.status = Status.AVAILABLE;
        battleShip = null;
    }

    @Override
    public String toString() {
        return coordinates.toString();
    }

    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    public Cell.Status getStatus() {
        return this.status;
    }

    public BattleShip getBattleShip() {
        return this.battleShip;
    }

    public void setUnavailable() {
        this.status = Status.UNAVAILABLE;
    }

    public void setBattleShip(BattleShip battleShip) {
        this.battleShip = battleShip;
        this.status = Status.BATTLE_SHIP;
    }

    public ShotResult takeShot() {
        ShotResult shotResult = ShotResult.MISS;

        switch (this.status) {
            case BATTLE_SHIP -> {
                shotResult = this.battleShip.hit();
                this.battleShip = null;
                this.status = Status.HIT;
            }
            case AVAILABLE, UNAVAILABLE -> {
                this.status = Status.MISS;
            }
            default -> throw new IllegalCallerException("Error: Cell already hit!");
        }

        return shotResult;
    }
}
