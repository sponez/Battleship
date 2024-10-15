package battleship.game.entities;

import java.util.Arrays;

public class BattleShip {
    public enum Type {
        AIRCRAFT_CARRIER("Aircraft Carrier", 5),
        BATTLESHIP("Battleship", 4),
        SUBMARINE("Submarine ", 3),
        CRUISER("Cruiser", 3),
        DESTROYER("Destroyer", 2);

        private final String inGameName;
        private final int length;

        Type(String inGameName, int length) {
            this.inGameName = inGameName;
            this.length = length;
        }

        public String getInGameName() {
            return inGameName;
        }

        public int getLength() {
            return length;
        }
    }

    private final Cell[] occupiedCells;
    private final Cell[] cellsAround;
    private final int size;
    private int aliveCells;

    public BattleShip(Type type, Cell[] occupiedCells, Cell[] cellsAround) {
        this.occupiedCells = occupiedCells;
        this.cellsAround = cellsAround;
        this.size = occupiedCells.length;
        this.aliveCells = occupiedCells.length;

        if (type.getLength() != occupiedCells.length) {
            throw new IllegalArgumentException(
                String.format(
                    "Error: Length of %s is invalid",
                    type.getInGameName()
                )
            );
        }
    }

    @Override
    public String toString() {
        return String.format(
            """
            Length: %d
            Parts: %s""",
            this.size,
            Arrays.toString(this.occupiedCells)
        );
    }

    public void place() throws IllegalCallerException {
        for (Cell occupiedCell: occupiedCells) {
            if (occupiedCell.getStatus() != Cell.Status.AVAILABLE) {
                throw new IllegalCallerException("Error: You placed it too close to another one. Try again.");
            }

            occupiedCell.setBattleShip(this);
        }

        for (Cell cellAround: cellsAround) {
            cellAround.setUnavailable();
        }
    }

    public ShotResult hit() {
        if (this.aliveCells == 0) {
            throw new IllegalCallerException("Ship already destroyed!");
        }

        if (--this.aliveCells == 0) {
            return ShotResult.SANK_SHIP;
        }

        return ShotResult.HIT;
    }
}
