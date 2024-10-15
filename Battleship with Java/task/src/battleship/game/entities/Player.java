package battleship.game.entities;

import battleship.game.BattleShipGame;

import java.util.InputMismatchException;

public class Player {
    private final Board board;
    private int aliveBattleShips;

    public Player(Board board) {
        this.board = board;
        this.aliveBattleShips = 0;
    }

    private BattleShip createBattleShip(BattleShip.Type battleShipType) throws InputMismatchException, IndexOutOfBoundsException {
        Coordinates firstCoordinates = BattleShipGame.In.nextCoordinates();
        Coordinates secondCoordinates = BattleShipGame.In.nextCoordinates();

        int upAround;
        int downAround;
        int leftAround;
        int rightAround;
        int shipSize;
        int cellsAroundAmount;
        Cell[] shipCells;
        Cell[] cellsAround;

        if (
            firstCoordinates.vertical() > secondCoordinates.vertical() ||
                firstCoordinates.horizontal() > secondCoordinates.horizontal()
        ) {
            Coordinates help = firstCoordinates;
            firstCoordinates = secondCoordinates;
            secondCoordinates = help;
        }

        upAround = Math.max(board.getFirstLetter(), firstCoordinates.vertical() - 1);
        downAround = Math.min(secondCoordinates.vertical() + 1, board.getLastLetter());
        leftAround = Math.max(board.getFirstNumber(), firstCoordinates.horizontal() - 1);
        rightAround = Math.min(secondCoordinates.horizontal() + 1, board.getLastNumber());

        shipSize = Math.max(
            (int) secondCoordinates.vertical() - (int) firstCoordinates.vertical() + 1,
            secondCoordinates.horizontal() - firstCoordinates.horizontal() + 1
        );
        cellsAroundAmount = (downAround - upAround + 1) * (rightAround - leftAround + 1) - shipSize;

        shipCells = new Cell[shipSize];
        cellsAround = new Cell[cellsAroundAmount];

        for (int v = upAround, i = 0, j = 0; v <= downAround; ++v) {
            for (int h = leftAround; h <= rightAround; ++h) {
                if (
                    v >= firstCoordinates.vertical() && v <= secondCoordinates.vertical() &&
                        h >= firstCoordinates.horizontal() && h <= secondCoordinates.horizontal()
                ) {
                    shipCells[i++] = board.getCell(new Coordinates((char) v, h));
                } else {
                    cellsAround[j++] = board.getCell(new Coordinates((char) v, h));
                }
            }
        }

        return new BattleShip(battleShipType, shipCells, cellsAround);
    }

    public void placeBattleShip(BattleShip.Type battleShipType) throws InputMismatchException, IllegalCallerException {
        BattleShip newBattleShip = this.createBattleShip(battleShipType);
        newBattleShip.place();
        ++aliveBattleShips;
    }

    public void showUncensoredBoard() {
        board.showUncensored();
    }

    public void showCensoredBoard() {
        board.showCensored();
    }

    public ShotResult processShot(Coordinates coordinates) {
        Cell hitCell = board.getCell(coordinates);
        ShotResult shotResult = hitCell.takeShot();

        if (shotResult == ShotResult.SANK_SHIP && --aliveBattleShips == 0) {
            return ShotResult.DEFEAT;
        }

        return shotResult;
    }

    public ShotResult makeShot(Player anotherPlayer) {
        Coordinates shotCoordinates;
        shotCoordinates = BattleShipGame.In.nextCoordinates();
        return anotherPlayer.processShot(shotCoordinates);
    }
}
